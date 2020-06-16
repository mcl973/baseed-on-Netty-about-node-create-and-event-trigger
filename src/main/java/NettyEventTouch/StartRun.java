package NettyEventTouch;

import NettyEventTouch.NettyModel.NettyInit;
import NettyEventTouch.NettyModel.NettyInit2;

public class StartRun {
    public static void main(String[] args) {
        new Thread(new NettyInit()).start();
    }
}
