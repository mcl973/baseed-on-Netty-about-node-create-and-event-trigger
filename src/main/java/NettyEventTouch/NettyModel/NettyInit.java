package NettyEventTouch.NettyModel;

import NettyEventTouch.ArgsInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyInit  implements Runnable{
    private EventLoopGroup boss,worker;
    private void init(){
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
    }
    public void config() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,worker).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

                ch.pipeline()
                        .addLast(new StringDecoder())  //添加加码器
                        .addLast(new StringEncoder())  //添加解码器
                        .addLast(new EventTouchHandler());
            }
        });
        ChannelFuture future = serverBootstrap.bind(ArgsInfo.hostname, ArgsInfo.port).sync();
        future.channel().closeFuture().sync();
    }

    public NettyInit(){
    }

    @Override
    public void run() {
        init();
        try {
            config();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
