
package com.jiec.contact.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.jiec.contact.LoginPhoneActivity;
import com.jiec.contact.MainActivity;
import com.jiec.contact.MyApplication;
import com.jiec.utils.LogUtil;

public class AppReceiver extends BroadcastReceiver {

    private static boolean mIsListenered = false;

    private static boolean mIsOFFHOOD = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        LogUtil.d("onReceive action = " + action);

        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(action)) {
            LogUtil.d("去电了");

        } else if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            // 开机启动MainActivity
            Intent mainIntent = new Intent(context, LoginPhoneActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        } else if (!mIsListenered) {
            mIsListenered = true;
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    PhoneStateListener listener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    LogUtil.d("CALL_STATE_IDLE  挂断");
                    if (mIsOFFHOOD) {
                        mIsOFFHOOD = false;
                        Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getContext().startActivity(intent);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    mIsOFFHOOD = true;
                    LogUtil.d("CALL_STATE_OFFHOOK 接听");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    LogUtil.d("响铃:来电号码" + incomingNumber);
                    // 输出来电号码
                    break;
            }
        }
    };

}
