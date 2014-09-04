
package com.jiec.contact.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.json.JSONObject;

import android.util.Log;

import com.jiec.utils.ToastUtil;

public class ContactSocket {

    public interface RespondListener {
        public void onSuccess(int cmd, JSONObject object);

        public void onFailed(int cmd, String reason);
    }

     private static String SERVER_IP = "192.168.0.105";
    //private static String SERVER_IP = "192.168.1.138";

    // private static String SERVER_IP = "114.215.153.4";

    private static int SERVER_PORT = 9999;

    private Socket mSocket = null;

    private boolean mConnected = false;

    private RespondListener mListener = null;

    private int mSeq = 0;

    private static int sSeq = 0;

    public static int getSeq() {
        return sSeq++;
    }

    public ContactSocket() {
        connect();
    }

    public void send(JSONObject object, RespondListener listener) {
        try {
            Log.i("test", object.toString());
            ObjectOutputStream oos = new ObjectOutputStream(mSocket.getOutputStream());
            oos.writeObject(object.toString());

            mSeq = object.getInt("seq");

            mListener = listener;
            
            ObjectInputStream ois = new ObjectInputStream(mSocket.getInputStream());
            String o = (String) ois.readObject();
            JSONObject jo = new JSONObject(o);

            if (mListener != null) {
                if (jo.getInt("seq") == mSeq) {
                    if (jo.getInt("result") == 1) {
                        mListener.onSuccess(mSeq, jo);
                    } else {
                        mListener.onFailed(mSeq, "密码错误");
                    }
                }
            }
            
            ois.close();             
            oos.close();
            mSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void connect() {
        try {
            mSocket = new Socket(SERVER_IP, SERVER_PORT);

            if (mSocket.isConnected()) {
                mConnected = true;
            }        

        } catch (Exception e) {
            ToastUtil.showMsg("本地网络出现问题或者服务器中断，请确定本地网络！如果本地正常请联系负责人");
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    public void closeSocket() {
        mConnected = false;
        if (mSocket != null) {
            try {
                mSocket.close();
                mSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Thread mThread = new Thread(new Runnable() {

        public void run() {
            while (mConnected) {
                // 不停的读取从服务器端发来的消息
                try {
                    if (mSocket == null || mSocket.isClosed()) {
                        closeSocket();
                        return;
                    } else {
                        ObjectInputStream ois = new ObjectInputStream(mSocket.getInputStream());
                        String o = (String) ois.readObject();
                        JSONObject jo = new JSONObject(o);

                        if (mListener != null) {
                            if (jo.getInt("seq") == mSeq) {
                                if (jo.getInt("result") == 1) {
                                    mListener.onSuccess(mSeq, jo.getJSONObject("contacts"));
                                } else {
                                    mListener.onFailed(mSeq, "密码错误");
                                }
                            }
                        }
                        
                        ois.close();
                        closeSocket();
                    }

                    Thread.sleep(100);

                } catch (Exception e) {
                     e.printStackTrace();
                }
            }
        }
    });
}
