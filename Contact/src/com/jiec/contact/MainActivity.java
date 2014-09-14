
package com.jiec.contact;

import android.app.NotificationManager;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.Toast;

import com.jiec.contact.model.CompanyModel;
import com.jiec.contact.model.ContactModel;
import com.jiec.utils.FTPClientUtils;

public class MainActivity extends TabActivity {

    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.main, tabHost.getTabContentView(), true);

        Intent contactIntent = new Intent();
        contactIntent.setClass(this, MyContactActivity.class);
        tabHost.addTab(tabHost.newTabSpec("联系人").setIndicator("联系人").setContent(contactIntent));

        Intent recordIntent = new Intent();
        recordIntent.setClass(this, MyRecordActivity.class);
        tabHost.addTab(tabHost.newTabSpec("通讯记录").setIndicator("通讯记录").setContent(recordIntent));

        // Intent jobIntent = new Intent();
        // jobIntent.setClass(this, MyJobActivity.class);
        // tabHost.addTab(tabHost.newTabSpec("我的任务").setIndicator("我的任务").setContent(jobIntent));
        //
        // Intent settingIntent = new Intent();
        // settingIntent.setClass(this, SettingActivity.class);
        // tabHost.addTab(tabHost.newTabSpec("设置").setIndicator("设置").setContent(settingIntent));

        CompanyModel.getInstance().requestCompanies();

        FTPClientUtils.updateFile();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                ContactModel.getInstance().finish();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}
