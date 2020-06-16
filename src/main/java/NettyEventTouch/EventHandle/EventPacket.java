package NettyEventTouch.EventHandle;


import NettyEventTouch.Nodes.Node;
import io.netty.channel.Channel;

public class EventPacket {
    private int code;
    private Node node;
    private Channel channel;
    private String data ;
    public EventPacket(int code,Node node,Channel channel,String data){
        this.code = code;
        this.node = node;
        this.channel = channel;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventPacket{" +
                "code=" + code +
                ", node=" + node +
                ", channel=" + channel +
                ", data='" + data + '\'' +
                '}';
    }
}
