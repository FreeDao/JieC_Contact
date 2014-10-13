
package com.jiec.contact;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

import com.jiec.contact.model.ContactModel;
import com.jiec.contact.model.RecordModel;
import com.jiec.contact.model.UserModel;
import com.jiec.utils.LogUtil;
import com.jiec.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 描述:
 * 
 * @author jiec
 * @since 2014-10-13 上午11:07:35
 */
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

        for (int i = 0; i < getTabWidget().getChildCount(); i++) {
            // 修改显示字体大小
            TextView tv = (TextView) getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(18);
        }

        if (!UserModel.getInstance().checkPhoneNumber(this)) {
            ToastUtil.showMsg("请更换sim卡，该卡不是公司分配的卡");

        }

        if (UserModel.getInstance().isUserLogined()) {
            startService(new Intent(this, SaveFileService.class));
        }

        setTitle("手机:"
                + UserModel.getInstance().getPhoneNumber()
                + "  用户:"
                + (UserModel.getInstance().isUserLogined() ? UserModel.getInstance().getUserId()
                        : "无"));

    }

    private void exitApp() {
        ContactModel.getInstance().finish();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        LogUtil.d("featrueId = " + featureId + ", item id = " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_login_user:
                loginUser();
                break;
            case R.id.action_logout_user:
                logoutUser();
                break;

            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void loginUser() {
        if (!UserModel.getInstance().isPhoneLogined()) {
            ToastUtil.showMsg("手机号码无效");
            return;
        }

        if (UserModel.getInstance().isUserLogined()) {
            ToastUtil.showMsg("用户已登录，请先退出用户");
        } else {
            startActivity(new Intent(MainActivity.this, LoginUIDActivity.class));
            finish();
        }
    }

    private void logoutUser() {
        if (!UserModel.getInstance().isUserLogined()) {
            ToastUtil.showMsg("尚未登录，请先登录用户");
            return;
        }
        ContactModel.getInstance().finish();
        RecordModel.getInstance().finish();
        UserModel.getInstance().setUserLogined(false);
        startActivity(new Intent(MainActivity.this, LoginUIDActivity.class));
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen"); // 统计页面
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen"); // 保证 onPageEnd 在onPause
                                                 // 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

}
