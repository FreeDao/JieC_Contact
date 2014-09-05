
package com.jiec.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.jiec.contact.MyApplication;

/**
 * 描述:显示提示信息
 * 
 * @author chenys
 * @since 2014-3-26 下午3:08:14
 */
public class ToastUtil {

    private static final int MSG_SHOW_MESSAGE = 0X01;

    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_SHOW_MESSAGE:
                    String text = (String) msg.obj;
                    Context context = MyApplication.getContext();
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    break;
            }
        };
    };

    /**
     * 显示提示信息
     * 
     * @param msg
     */
    public static void showMsg(String msg) {
        mHandler.obtainMessage(MSG_SHOW_MESSAGE, msg).sendToTarget();
    }
}
