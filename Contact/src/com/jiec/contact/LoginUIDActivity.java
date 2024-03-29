
package com.jiec.contact;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.jiec.contact.model.CompanyModel;
import com.jiec.contact.model.Protocal;
import com.jiec.contact.model.UserModel;
import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 描述:用户登录界面
 * 
 * @author jiec
 * @since 2014-10-13 上午11:07:35
 */
public class LoginUIDActivity extends Activity {

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private EditText mUserId, mUserPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_user_id);
        // lockKey();

        mUserId = (EditText) findViewById(R.id.et_user_id);
        mUserPasswd = (EditText) findViewById(R.id.et_user_passwd);

        // {
        // UserModel.getInstance().setUserId("8888");
        // startActivity(new Intent(LoginUIDActivity.this, MainActivity.class));
        //
        // finish();
        // UserModel.getInstance().setUserLogined(true);
        // CompanyModel.getInstance().requestCompanies();
        // }

        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mUserId.getText().length() < 1) {
                    ToastUtil.showMsg("请输入完整用户名");
                    return;
                } else if (mUserPasswd.getText().length() < 1) {
                    ToastUtil.showMsg("请输入密码");
                    return;
                }

                String str = "{seq:" + (ContactSocket.getSeq()) + ",cmd:"
                        + Protocal.CMD_LOGIN_USER_REQUEST + ",user_id:" + "\"" + mUserId.getText()
                        + "\"" + ",user_passwd:" + "\"" + mUserPasswd.getText() + "\"" + "}";
                JSONObject object = null;
                try {
                    object = new JSONObject(str);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                ContactSocket.getInstance().send(object, new RespondListener() {

                    @Override
                    public void onSuccess(int cmd, JSONObject object) {
                        ToastUtil.showMsg("登陆成功！");
                        UserModel.getInstance().setUserId(mUserId.getText().toString().trim());
                        startActivity(new Intent(LoginUIDActivity.this, MainActivity.class));

                        finish();
                        UserModel.getInstance().setUserLogined(true);
                        CompanyModel.getInstance().requestCompanies();
                    }

                    @Override
                    public void onFailed(int cmd, String reason) {
                        ToastUtil.showMsg(reason);
                    }
                });

            }
        });
    }

    private void lockKey() {
        int androidVersion = android.os.Build.VERSION.SDK_INT;
        if (androidVersion >= 14) {
            this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        } else {
            mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
        }
    }

    Runnable mDisableHomeKeyRunnable = new Runnable() {

        @Override
        public void run() {
            disableHomeKey();

        }
    };

    Handler mHandler = new Handler();

    public void disableHomeKey() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(LoginUIDActivity.this, MainActivity.class));
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
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
