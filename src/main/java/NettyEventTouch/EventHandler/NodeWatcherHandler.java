package NettyEventTouch.EventHandler;

import NettyEventTouch.EventHandle.Container;
import NettyEventTouch.EventHandle.EventHandle;
import NettyEventTouch.Nodes.Node;
import io.netty.channel.Channel;

import java.util.concurrent.CopyOnWriteArrayList;

public class NodeWatcherHandler  implements NodeHandler<Void>{
    private Node node;
    private Channel channel;
    public NodeWatcherHandler( Node node,Channel channel){
        this.node = node;
        this.channel = channel;
    }

    @Override
    public Void handler() {
        if (Container.NodeWatchList.containsKey(node))
            Container.NodeWatchList.get(node).add(channel);
        else{
            CopyOnWriteArrayList<Channel> channels = new CopyOnWriteArrayList<>();
            Container.NodeWatchList.put(node,channels);
            Container.NodeWatchList.get(node).add(channel);
        }
        return null;
    }
}
