
package com.jiec.contact.socket;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactSocket {

    public interface RespondListener {
        public void onSuccess(int cmd, JSONObject object);

        public void onFailed(int cmd, String reason);
    }

    // private static String SERVER_IP = "192.168.0.123";

    // private static String SERVER_IP = "192.168.1.138";

    // private static String SERVER_IP = "120.24.58.159";

    private static String SERVER_IP = "114.215.153.4";

    private static int SERVER_PORT = 9000;

    private static int sSeq = 0;

    private MinaClientHandler mMinaClientHandler;

    private static ContactSocket sContactSocket = null;

    public static ContactSocket getInstance() {
        if (sContactSocket == null) {
            sContactSocket = new ContactSocket();
        }
        return sContactSocket;
    }

    public static int getSeq() {
        return sSeq++;
    }

    private ContactSocket() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                connect();
            }
        }).start();
    }

    public void send(final JSONObject object, final RespondListener listener) {
        try {
            while (mMinaClientHandler == null) {
            }
            mMinaClientHandler.send(object, listener, object.getInt("seq"));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    public void connect() {

        NioSocketConnector connector = new NioSocketConnector();
        // 创建接收数据的过滤器
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();

        TextLineCodecFactory lineCodec = new TextLineCodecFactory();
        lineCodec.setDecoderMaxLineLength(1024 * 1024); // 1M
        lineCodec.setEncoderMaxLineLength(1024 * 1024); // 1M

        chain.addLast("codec", new ProtocolCodecFilter(lineCodec)); // 行文本解析 //
                                                                    // 设定这个过滤器将一行一行读数据
        chain.addLast("log", new LoggingFilter()); // 日志拦截

        mMinaClientHandler = new MinaClientHandler();
        connector.setHandler(mMinaClientHandler);
        connector.setConnectTimeout(30);
        // 连接到服务器
        ConnectFuture cf = connector.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
        // wait for the connection attempt to be finished
        cf.awaitUninterruptibly();
        cf.getSession().getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }
}
