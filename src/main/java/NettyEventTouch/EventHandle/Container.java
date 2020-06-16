package NettyEventTouch.EventHandle;

import NettyEventTouch.Nodes.Node;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Container {
    /**
     * 所有的节点容器
     * <String, Channel>   <点名称，创建节点的channel>
     */
    public static Map<Node, Channel> AllClient = new ConcurrentHashMap<>();
    /**
     * 某个节点的处理事件的容器
     * 顺序处理事件，增、删、改、查
     * <String, BlockingQueue<EventPacket>>  <节点，关于这个节点的事件的队列>
     */
    public static Map<Node, BlockingQueue<EventPacket>> NodeHandleEventQueue = new ConcurrentHashMap<>();
    /**
     * 某个节点的监听的client
     * <String, CopyOnWriteArrayList<Channel>><节点名称，监听这个节点的所有的client>
     */
    public static Map<Node, CopyOnWriteArrayList<Channel>> NodeWatchList = new ConcurrentHashMap<>();

    /**
     * 保存所有的节点的容器
     * <String,Node>   <节点名称，节点实例>
     */
    public static Map<String,Node> NodeList = new ConcurrentHashMap<>();

}
