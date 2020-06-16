package NettyEventTouch.EventHandle;

import NettyEventTouch.ArgsInfo;
import NettyEventTouch.EventHandler.*;
import NettyEventTouch.Nodes.Node;
import NettyEventTouch.ThreadPool.ExcutorService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class EventHandle implements Runnable{
    private ThreadPoolExecutor poolExecutor = ExcutorService.getDefaultThreadPoolExcutor();
    private BlockingQueue<EventPacket> eventqueue;
    public EventHandle(BlockingQueue<EventPacket> eventqueue){
        this.eventqueue = eventqueue;
    }

    @Override
    public void run() {
//        如果节点被删除了，那么就需要将这个队列置位空，否则，这个循环会一直占着
        while (eventqueue!=null){
            try {
                /**
                 * 从队列中拿取数据
                 */
                EventPacket event = eventqueue.take();
                int code = event.getCode();
                Node node = event.getNode();
                switch (code){
                    case ArgsInfo.CTREATE:
                        new NodeCreateHandler(node,event.getChannel()).handler();
                        break;
                    case ArgsInfo.DELETE:
                        new NodeDeleteHandler(node,event.getChannel()).handler();
                        break;
                    case ArgsInfo.CHANGE:
                        String data = event.getData();
                        if (data!=null) {
                            Node newnode = new Node(node.getNode(),data);
                            new NodeChangeHandler(node,newnode,event.getChannel()).handler();
                        }
                        break;
                    case ArgsInfo.ADDWATCH:
                        new NodeWatcherHandler(node,event.getChannel()).handler();
                        break;
                    case ArgsInfo.SELECT:
                        new NodeSelectHandler(node,event.getChannel()).handler();
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
