
package com.jiec.contact.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.jiec.contact.LoginPhoneActivity;
import com.jiec.contact.MainActivity;
import com.jiec.contact.model.RecordModel;
import com.jiec.utils.LogUtil;
import com.jiec.utils.PhoneUtils;

public class AppReceiver extends BroadcastReceiver {

    private static boolean mIsListenered = false;

    private static boolean mIsOFFHOOD = false;

    private static boolean sLogined = false;

    public static void setLogined(boolean logined) {
        sLogined = logined;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        LogUtil.d("onReceive action = " + action);

        if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            // 开机启动MainActivity
            Intent mainIntent = new Intent(context, LoginPhoneActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
            return;
        }

        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(action)) {
            LogUtil.d("去电了");

        } else if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    LogUtil.e("hg", "电话状态……RINGING");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    LogUtil.e("hg", "电话状态……OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    LogUtil.e("hg", "电话状态……IDLE");

                    Intent mainIntent = new Intent(context, MainActivity.class);
                    if (!sLogined) {
                        mainIntent = new Intent(context, LoginPhoneActivity.class);
                    }
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(mainIntent);
                    // 挂电话都会走到这个地方
                    RecordModel.getInstance().pushRecordToServer(
                            PhoneUtils.getRecordsFromContact(context));

                    break;
            }
        }
    }
}
