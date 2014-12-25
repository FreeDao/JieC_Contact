
package com.jiec.contact.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MainServer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // new ContactServer();

        // 创建一个非阻塞的serever端socket，用Nio
        SocketAcceptor acceptor = new NioSocketAcceptor();
        // 创建接收数据的过滤器
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        // 设定这个过滤器规则(将一行一行读取数据)

        TextLineCodecFactory lineCodec = new TextLineCodecFactory();
        lineCodec.setDecoderMaxLineLength(10 * 1024 * 1024); // 10M
        lineCodec.setEncoderMaxLineLength(10 * 1024 * 1024); // 10M

        chain.addLast("codec", new ProtocolCodecFilter(lineCodec)); // 行文本解析
                                                                    // //
                                                                    // 设定这个过滤器将一行一行读数据

        chain.addLast("myChin", new ProtocolCodecFilter(lineCodec));
        // 设定服务器端的消息处理器：一个MinaServerHandler对象
        acceptor.setHandler(new MinaServerHandler());
        // 服务器端绑定的端口
        int bindPort = 9000;
        // 绑定端口，启动服务器
        try {
            acceptor.bind(new InetSocketAddress(bindPort));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Mina server is listing on:=" + bindPort);

    }
}
