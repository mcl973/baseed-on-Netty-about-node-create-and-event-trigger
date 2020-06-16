package NettyEventTouch.NettyModel;

import NettyEventTouch.ArgsInfo;
import NettyEventTouch.EventHandle.Container;
import NettyEventTouch.EventHandle.EventHandle;
import NettyEventTouch.EventHandle.EventPacket;
import NettyEventTouch.Nodes.Node;
import NettyEventTouch.ThreadPool.ExcutorService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jdk.internal.org.objectweb.asm.util.CheckAnnotationAdapter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class EventTouchHandler extends SimpleChannelInboundHandler<String> {
    private ThreadPoolExecutor poolExecutor = ExcutorService.getDefaultThreadPoolExcutor();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        /**
         * 1.获取channel
         * 2.获取事件和事件对应的节点的名称
         * 3.将事件装入节点事件容器
         */
//1
        Channel channel = ctx.channel();
        /**
         * 1.节点名称
         * 2.具体的事件的代码
         */
        String[] split = msg.split("#####");
//2
        String node = split[0];
        int event = Integer.parseInt(split[1]);
//3
//        判断有无数据
        String data = null;
        if (split.length>2)
            data = split[2];

        if (!Container.NodeList.containsKey(node)) {
            /**
             * 创建节点
             * 生成事件对象并将其放置到具体的事件队列中
             */
            Node newnode = new Node(node,data);
            Container.NodeList.put(node,newnode);

            EventPacket ep = new EventPacket(event,newnode,channel,data);
            LinkedBlockingQueue<EventPacket> eventPackets = new LinkedBlockingQueue<>();
            eventPackets.offer(ep);
            Container.NodeHandleEventQueue.put(newnode,eventPackets);
//           开启这个对这个节点的数据的处理，只有创建这个节点的时候才会开启
//            所以有一个难受的就是如果创建的活动的节点很多的话，那么会有很多的线程。
//            所以这里需要优化。
            poolExecutor.submit(new EventHandle(Container.NodeHandleEventQueue.get(newnode)));
        }else{
            Node node1 = Container.NodeList.get(node);
            /**
             * 先判断一下事件的类型，如果是创建节点那么就表示这个节点已存在了，拒绝创建
             * 如果不是那么就执行下面的操作
             */
            if (event == ArgsInfo.CTREATE){
                channel.writeAndFlush("节点已存在");
            }else {
//                生成具体的事件对象，并装入事件等待队列中
                EventPacket ep = new EventPacket(event,node1,channel,data);
                BlockingQueue<EventPacket> eventPackets1 = Container.NodeHandleEventQueue.get(node1);
                eventPackets1.offer(ep);
            }
        }
    }
}
