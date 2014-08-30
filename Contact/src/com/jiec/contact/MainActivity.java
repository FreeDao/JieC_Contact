
package com.jiec.contact;

import com.jiec.contact.socket.ContactSocket;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.main, tabHost.getTabContentView(), true);

        Intent contactIntent = new Intent();
        contactIntent.setClass(this, MyContactActivity.class);
        tabHost.addTab(tabHost.newTabSpec("联系人").setIndicator("联系人").setContent(contactIntent));

        Intent recordIntent = new Intent();
        recordIntent.setClass(this, MyRecordActivity.class);
        tabHost.addTab(tabHost.newTabSpec("通讯记录").setIndicator("通讯记录").setContent(recordIntent));

        Intent jobIntent = new Intent();
        jobIntent.setClass(this, MyJobActivity.class);
        tabHost.addTab(tabHost.newTabSpec("我的任务").setIndicator("我的任务").setContent(jobIntent));

        Intent settingIntent = new Intent();
        settingIntent.setClass(this, SettingActivity.class);
        tabHost.addTab(tabHost.newTabSpec("设置").setIndicator("设置").setContent(settingIntent));

    }
    
    @Override
    protected void onDestroy() {
    	ContactSocket.getInstance().closeSocket();
    	super.onDestroy();
    }

}
