package NettyEventTouch.EventHandler;

import NettyEventTouch.EventHandle.Container;
import NettyEventTouch.Nodes.Node;
import io.netty.channel.Channel;

public class NodeCreateHandler implements NodeHandler<Void>{
    private Node node;
    private Channel channel;
    public NodeCreateHandler( Node node,Channel channel){
        this.node = node;
        this.channel = channel;
    }

    public Void handler(){
        /**
         * 将节点与channel绑定
         */
        Container.AllClient.put(node,channel);
        return null;
    }

}
