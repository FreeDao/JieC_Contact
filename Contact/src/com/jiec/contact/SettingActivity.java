
package com.jiec.contact;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.jiec.contact.model.UserModel;
import com.umeng.analytics.MobclickAgent;

/**
 * 描述:设置界面
 * 
 * @author jiec
 * @since 2014-10-13 上午11:07:35
 */
public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ToggleButton tButton = (ToggleButton) findViewById(R.id.toggleButton1);
        tButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                UserModel.getInstance().setIsNotAllowBlackCall(arg1);
            }
        });

        tButton.setChecked(UserModel.getInstance().isIsNotAllowBlackCall());
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
