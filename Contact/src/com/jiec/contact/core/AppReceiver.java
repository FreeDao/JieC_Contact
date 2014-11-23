
package com.jiec.contact.core;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.jiec.contact.MainActivity;
import com.jiec.contact.model.RecordModel;
import com.jiec.contact.model.UserModel;
import com.jiec.contact.widget.CorverCallScreen;
import com.jiec.utils.LogUtil;
import com.jiec.utils.PhoneNumUtils;

public class AppReceiver extends BroadcastReceiver {

    private static String number = "";// 定义一个监听电话号码

    private boolean isRecord = false;// 定义一个当前是否正在复制的标志

    private MediaRecorder recorder = null;// 媒体复制类

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        LogUtil.d("onReceive action = " + action);

        if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            // 开机启动MainActivity
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
            return;
        }

        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(action)) {
            number = this.getResultData();
            if (number.startsWith("+86")) {
                number = number.substring(3);
            }
            CorverCallScreen.getInstance().addCorverScreen(number);
            number = PhoneNumUtils.toStarPhoneNumber(number);

        } else if (action.equals("android.intent.action.PHONE_STATE")) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Bundle bundle = intent.getExtras();
                    number = bundle.getString("incoming_number");

                    if (number == null) {
                        number = "";
                    }
                    if (number.startsWith("+86")) {
                        number = number.substring(3);
                    }
                    CorverCallScreen.getInstance().addCorverScreen(number);

                    number = PhoneNumUtils.toStarPhoneNumber(number);

                    LogUtil.e("hg", "电话状态……RINGING" + number);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    LogUtil.e("hg", "电话状态……OFFHOOK");

                    // 录制声音，这是录音的核心代码
                    LogUtil.e("msg", "recording " + number);

                    startRecord();

                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    LogUtil.e("hg", "电话状态……IDLE");

                    stopRecord();
                    CorverCallScreen.getInstance().removeCorverScreen();

                    enterApp(context);
                    break;
            }
        } else if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            enterApp(context);
        }
    }

    private void enterApp(Context context) {
        Intent mainIntent = new Intent(context, MainActivity.class);
        if (UserModel.getInstance().isUserLogined()) {
            RecordModel.getInstance().scanSystemRecord();
        }
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mainIntent);
    }

    private void startRecord() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);// 定义声音来自于麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);// 存储格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// 设置编码
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
        String date = sDateFormat.format(new java.util.Date());
        String fileName = date + "_" + UserModel.getInstance().getUserId() + "_" + number;
        File file = new File(Environment.getExternalStorageDirectory(), "contact");
        if (!file.exists()) {
            file.mkdirs();
        }
        recorder.setOutputFile(file.getAbsolutePath() + File.separator + fileName + ".amr");
        try {
            recorder.prepare();
            recorder.start(); // 开始刻录
            isRecord = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        number = null;
        if (recorder != null && isRecord) {
            LogUtil.e("msg", "record ok");
            recorder.stop();// 录音完成
            recorder.reset();
            recorder.release();
            isRecord = false;// 录音完成，改变状态标志
        }
    }
}
