
package com.jiec.contact;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiec.contact.core.AppReceiver;
import com.jiec.contact.model.Protocal;
import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.SIMCardInfo;
import com.jiec.utils.ToastUtil;

public class LoginPhoneActivity extends Activity {

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private EditText mPasswdEditText = null;

    private String mPhoneStr = null;

    private TextView mPhoneTV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_phone);
        lockKey();

        startActivity(new Intent(this, MainActivity.class));
        finish();

        mPasswdEditText = (EditText) findViewById(R.id.et_phone_passwd);

        Button loginBtn = (Button) findViewById(R.id.login_btn);

        mPhoneStr = new SIMCardInfo(this).getNativePhoneNumber();

        if (mPhoneStr == null) {
            mPhoneStr = "";
            loginBtn.setClickable(false);
            loginBtn.setEnabled(false);
            mPasswdEditText.setEnabled(false);
            mPasswdEditText.setClickable(false);

            TextView error_tv = (TextView) findViewById(R.id.error_tv);
            error_tv.setTextColor(Color.RED);
            error_tv.setText("检测不到SIM卡，请关机插上SIM卡或者检查SIM，确认无误再开机使用");
            ToastUtil.showMsg("检测不到SIM卡，请关机插上SIM卡或者检查SIM，确认无误再开机使用");
        }
        if (mPhoneStr.startsWith("+86")) {
            mPhoneStr = mPhoneStr.substring(3);
        }

        mPhoneTV = (TextView) findViewById(R.id.tv_phone_number);
        mPhoneTV.setText(mPhoneTV.getText() + mPhoneStr);

        loginBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPasswdEditText.getText().toString().length() < 1) {
                    ToastUtil.showMsg("密码不能为空");
                    return;
                }

                // TODO Auto-generated method stub
                String str = "{seq:" + (ContactSocket.getSeq()) + ",cmd:"
                        + Protocal.CMD_LOGIN_REQUEST + ",phoneNum:" + "\"" + mPhoneStr + "\""
                        + ",passwd:" + "\"" + mPasswdEditText.getText().toString().trim() + "\""
                        + "}";
                JSONObject object = null;
                try {
                    object = new JSONObject(str);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                new ContactSocket().send(object, new RespondListener() {

                    @Override
                    public void onSuccess(int cmd, JSONObject object) {
                        ToastUtil.showMsg("登陆成功！");
                        startActivity(new Intent(LoginPhoneActivity.this, LoginUIDActivity.class));
                        finish();

                        AppReceiver.setLogined(true);

                    }

                    @Override
                    public void onFailed(int cmd, String reason) {
                        // TODO Auto-generated method stub
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
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            // 监控/拦截菜单键
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
            // 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
        }
        return super.dispatchKeyEvent(event);
    }
}
