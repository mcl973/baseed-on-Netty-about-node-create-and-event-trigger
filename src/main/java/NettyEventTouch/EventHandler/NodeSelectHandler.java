package NettyEventTouch.EventHandler;

import NettyEventTouch.ArgsInfo;
import NettyEventTouch.EventHandle.EventPacket;
import NettyEventTouch.Nodes.Node;
import io.netty.channel.Channel;

/**
 * 这没有使用多线程技术
 * 按道理说读是可以实现多线程的额，但是在些方面没有实现所得机制，只是按照队列来实现串行化
 * 所以如果这里要实现度的多线程的话，会导致读脏的数据
 */
public class NodeSelectHandler implements NodeHandler<Void> {
    private Node node;
    private Channel channel;
    public NodeSelectHandler( Node node,Channel channel){
        this.node = node;
        this.channel = channel;
    }

    @Override
    public Void handler() {
        EventPacket ep = new EventPacket(ArgsInfo.SELECT, node, channel, node.getData());
        channel.writeAndFlush(ep.toString());
        return null;
    }
}
