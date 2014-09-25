
package com.jiec.contact;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.jiec.utils.FTPClientUtils;

public class SaveFileService extends Service {

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                mHandler.sendEmptyMessageDelayed(0, 1000 * 60 * 15);

                FTPClientUtils.updateFile();

                Log.e("test", "testService going");
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
        createNotification();
        super.onCreate();
    }

    private static final int NOTIFICATION_ID = Integer.MAX_VALUE - 1000;

    private void createNotification() {
        Notification notification = new Notification(R.drawable.ic_launcher,
                getText(R.string.app_name), System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "通讯录", "开启", pendingIntent);
        startForeground(NOTIFICATION_ID, notification);
    }

}
