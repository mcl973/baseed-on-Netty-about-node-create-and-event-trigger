package NettyEventTouch;

public class ArgsInfo {
    //    这个是建立本地的通信服务，用于和python的时间的通知
    public static int port = 8886;
    public static int port2 = 8887;
    public static String hostname = "localhost";

    public static final int Default_Thread_Number = 5;

    /**
     * 对于节点的操作
     * 创建
     * 删除
     * 改变
     * 添加监听器
     * 查看数据
     */
    public final static int CTREATE = 0;
    public final  static int DELETE = 1;
    public  final static int CHANGE = 2;
    public final  static int ADDWATCH = 3;
    public  final static int SELECT = 4;

}
