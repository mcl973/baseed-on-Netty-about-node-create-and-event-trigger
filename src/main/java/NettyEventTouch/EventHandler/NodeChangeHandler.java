package NettyEventTouch.EventHandler;

import NettyEventTouch.ArgsInfo;
import NettyEventTouch.EventHandle.Container;
import NettyEventTouch.EventHandle.EventPacket;
import NettyEventTouch.Nodes.Node;
import io.netty.channel.Channel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class NodeChangeHandler implements NodeHandler<Void>{
    private Node oldnode;
    private Node newnode;
    private Channel channel;
    public NodeChangeHandler( Node oldnode,Node newnode,Channel channel){
        this.oldnode = oldnode;
        this.newnode = newnode;
        this.channel = channel;
    }
    @Override
    public Void handler() {
        /**
         * 数据的迁移
         * 1.监听列表的迁移
         * 2.任务列表的迁移
         * 3.节点列表的迁移
         * 4.所有用户短端的迁移
         * 5.通知监听的用户
         */
        if (Container.NodeWatchList.containsKey(oldnode)) {
            CopyOnWriteArrayList<Channel> channels1 = Container.NodeWatchList.get(oldnode);
            Container.NodeWatchList.remove(oldnode);
            Container.NodeWatchList.put(newnode, channels1);
        }

        BlockingQueue<EventPacket> eventPackets = Container.NodeHandleEventQueue.get(oldnode);
        Container.NodeHandleEventQueue.remove(oldnode);
        Container.NodeHandleEventQueue.put(newnode,eventPackets);

        Container.NodeList.put(newnode.getNode(),newnode);

        Channel channel = Container.AllClient.get(oldnode);
        Container.AllClient.remove(oldnode);
        Container.AllClient.put(newnode,channel);

        CopyOnWriteArrayList<Channel> channels = Container.NodeWatchList.get(newnode);
        if (channels!=null) {
            for (Channel channel1 : channels) {
                EventPacket ep = new EventPacket(ArgsInfo.CHANGE, newnode, this.channel, "数据改变: "+newnode.getData());
                channel1.writeAndFlush(ep.toString());
            }
        }
        return null;
    }
}
