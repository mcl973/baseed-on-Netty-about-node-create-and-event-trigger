package NettyEventTouch.EventHandler;

import NettyEventTouch.ArgsInfo;
import NettyEventTouch.EventHandle.Container;
import NettyEventTouch.EventHandle.EventPacket;
import NettyEventTouch.Nodes.Node;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class NodeDeleteHandler implements NodeHandler<Void>{
    private Node node;
    private Channel channel;
    public NodeDeleteHandler( Node node,Channel channel){
        this.node = node;
        this.channel = channel;
    }

    @Override
    public Void handler() {
        if(channel.remoteAddress().toString().equals(Container.AllClient.get(node).remoteAddress().toString())){
            Container.AllClient.remove(node);
        }
        /**
         * 处理其他的时间如：节点的后续的任务队列要删除，节点的监听列表要删除，要通知到各个监听这个节点的client
         * 这里不能有线层来处理的原因是防止出错
         */
        BlockingQueue<EventPacket> eventPackets = Container.NodeHandleEventQueue.get(node);
//        置空，好释放线程资源
        eventPackets = null;
        Container.NodeHandleEventQueue.remove(node);
        Map<Node, CopyOnWriteArrayList<Channel>> nodeWatchList = Container.NodeWatchList;
        if (nodeWatchList!=null&&nodeWatchList.size()>0) {
            EventPacket eventPacket = new EventPacket(ArgsInfo.DELETE, node, channel, "节点删除");
            CopyOnWriteArrayList<Channel> channels = nodeWatchList.get(node);
            for (Channel channel1 : channels) {
                channel1.writeAndFlush(eventPacket.toString());
            }
        }
        return null;
    }
}
