package NettyEventTouch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class client {
    private static int[] ports = {8886,8887};
    private static AtomicInteger i = new AtomicInteger();

    private static Socket socket = null;
    private static InputStream inputStream = null;
    private static OutputStream outputStream = null;
    private static Map<String,SocketDetail> socketmap = new ConcurrentHashMap<>();
    public client() throws Exception {
        socketinit();
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        new receive().start();
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        while (!s.equals("eof")){
            try {
                outputStream.write(s.getBytes());
                outputStream.flush();
            }catch (Exception e){
                getNewConnect();
                if (outputStream!=null){
                    outputStream.write(s.getBytes());
                    outputStream.flush();
                }else{
                    System.out.println("远程连接断开");
                    break;
                }
            }
            s = scanner.nextLine();
        }
    }

    public void socketinit(){
        for (int port : ports) {
            String socketkey = "localhost"+port;
            SocketDetail socketval = new SocketDetail("localhost", port, true);
            socketmap.put(socketkey,socketval);
        }
        try {
            socket = new Socket("localhost", ports[i.incrementAndGet()]);
        }catch (Exception e){
            try {
                socket = new Socket("localhost", ports[i.incrementAndGet()]);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    static class receive extends Thread{

        public receive(){
        }
        @Override
        public void run() {
            while (true) {
                byte[] bytes = new byte[1024];
                int read = 0;
                try {
                    try {
                        read = inputStream.read(bytes);
                    }catch (Exception e){
                        getNewConnect();
                        if (inputStream == null){
                            System.out.println("远程连接断开");
                            break;
                        }else
                            read = inputStream.read(bytes);
                    }
                    while (read > 0) {
                        String s = new String(bytes);
                        char[] chars = s.toCharArray();
                        StringBuilder str = new StringBuilder();
                        for (char aChar : chars) {
                            if (aChar!=' ')
                                str.append(aChar);
                        }
                        System.out.println(str.toString());
                        try {
                            read = inputStream.read(bytes);
                        }catch (Exception e){
                            getNewConnect();
                            if (inputStream == null){
                                System.out.println("远程连接断开");
                                break;
                            }else
                                read = inputStream.read(bytes);
                        }
                    }
                } catch (IOException e) {
                    getNewConnect();
                    if (inputStream == null) {
                        System.out.println("远程连接断开");
                        break;
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SocketDetail{
        private String hostname;
        private int port;
        private boolean isfalse;

        public SocketDetail(String hostname,int port,boolean isfalse){
            this.hostname = hostname;
            this.port = port;
            this.isfalse = isfalse;
        }

        public String getHostname() {
            return hostname;
        }

        public int getPort() {
            return port;
        }

        public boolean isIsfalse() {
            return isfalse;
        }

        public void setIsfalse(boolean isfalse) {
            this.isfalse = isfalse;
        }
    }
    public static synchronized void getNewConnect(){
            boolean keepAlive = true;
            try {
                keepAlive = socket.getKeepAlive();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        /**
         * 这里是为了防止出现socket已经跟换而其他的程序进入到这里后有进行跟换socket的情况。
         *
         */
        if (keepAlive)
                return;
            try {
//                其实这个函数里已经是一个安全的了，没有必要再去弄一个AtomicInteger再来保证id增长的
//                安全性
                int k = i.incrementAndGet() % 2;
                System.out.println(k + "    " + ports[k]);
                String socketkey = "localhost"+ports[k];
                SocketDetail socketDetail = socketmap.get(socketkey);
                boolean isfalse = socketDetail.isIsfalse();
                /**
                 * 如果为fale，那么表示一个循环已经到头了，但是没有一个连接是可用的，那么就表示远程的服务器集群已经全部都死掉了
                 * 这里有一个情况就是如果现在服务器集群中有一个服务器起来了，那么此时会漏掉，但是这里的处理速度是很快的，
                 * 所以基本不会有漏掉的情况。即服务器在重启，而此时刚好在建立连接所以失败了，但这个是无法避免的，client不可能一直在判断他是否重启。
                 * 除非判断是死循环，不然不管你设置的循环次数是多少总会有一个疑问就是如果恰巧最后一次时有服务器正在重启而我刚好建立连接
                 * 所以，为了防止卡死，所以这里只设置了一次的循环。
                 */
                if (!isfalse)
                    return;
                socket = new Socket("localhost", ports[k]);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                /**
                 * 如果有一个连接成功了，那么就需要将其他的连接的状态恢复，防止其他的连接的server端有恢复的情况
                 */
                socketmap.forEach((str,action)->action.setIsfalse(true));
            } catch (Exception e) {
                getNewConnect();
                System.out.println("远程连接断开");
            }
    }
    public static void main(String[] args) throws Exception {
        new client();
    }
}
