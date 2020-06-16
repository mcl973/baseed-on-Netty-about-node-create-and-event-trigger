package NettyEventTouch;

import NettyEventTouch.NettyModel.NettyInit;
import NettyEventTouch.NettyModel.NettyInit2;

public class StartRun2 {
    public static void main(String[] args) {
        new Thread(new NettyInit2()).start();
    }
}
