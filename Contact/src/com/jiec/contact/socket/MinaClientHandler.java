
package com.jiec.contact.socket;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.LogUtil;

//Mina客户端消息处理器类  
public class MinaClientHandler extends IoHandlerAdapter {

    IoSession mIoSession = null;

    Map<Integer, RespondListener> mListeners = new HashMap<Integer, ContactSocket.RespondListener>();

    public void send(final JSONObject object, final RespondListener listener, int seq) {
        while (mIoSession == null)
            ;
        mIoSession.write(object);

        mListeners.put(seq, listener);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        LogUtil.e("messageReceived : " + message.toString());

        JSONObject object = new JSONObject(message.toString());

        RespondListener listener = mListeners.get(object.getInt("seq"));

        if (listener != null) {

            if (object.getInt("result") == 1) {
                listener.onSuccess(object.getInt("seq"), object);
            } else {
                listener.onFailed(object.getInt("seq"), object.getString("reason"));
            }
            mListeners.remove(object.getInt("seq"));
        }

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("one Client sessionClosed");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("one Client sessionCreated" + session.getRemoteAddress());
        mIoSession = session;
    }

}
