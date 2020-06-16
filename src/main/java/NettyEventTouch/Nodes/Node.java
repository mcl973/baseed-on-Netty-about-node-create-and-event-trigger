package NettyEventTouch.Nodes;

public final class Node{
    private final String node;
    private final String data;
    public Node(String node,String data){
        this.node = node;
        this.data = data;
    }

    public String getNode() {
        return node;
    }

    public String getData() {
        return data;
    }

    /*
        只是用node的原因是防止数据的改变而导致整个node的不可用
     */
    @Override
    public int hashCode() {
        return node.length()^(node.length()/2);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getNode().equals(((Node)obj).getNode());
    }

    @Override
    public String toString() {
        return "Node{" +
                "node='" + node + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
