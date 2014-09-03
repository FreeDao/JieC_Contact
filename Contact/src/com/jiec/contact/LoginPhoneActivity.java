
package com.jiec.contact;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiec.contact.model.Protocal;
import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.LockLayer;
import com.jiec.utils.SIMCardInfo;
import com.jiec.utils.ToastUtil;

public class LoginPhoneActivity extends Activity {

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
        mPhoneStr = new SIMCardInfo(this).getNativePhoneNumber();
        if (mPhoneStr.startsWith("+86")) {
        	mPhoneStr = mPhoneStr.substring(3);
        }
        
        mPhoneTV = (TextView) findViewById(R.id.textView1);
        mPhoneTV.setText(mPhoneTV.getText() + mPhoneStr);

        mPasswdEditText = (EditText) findViewById(R.id.editText1);
        
        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	if (mPasswdEditText.getText().toString().length() < 1) {
            		ToastUtil.showMsg("密码不能为空");
            		return;
            	}
            	
                // TODO Auto-generated method stub
            	String str = "{seq:" + (ContactSocket.sSeq++) + 
            			",cmd:" + Protocal.CMD_LOGIN_REQUEST + 
            			",phoneNum:" + "\"" + mPhoneStr + "\"" + 
            			",passwd:" + "\"" + mPasswdEditText.getText().toString().trim() +  "\"" + 
            			"}";
            	JSONObject object = null;
				try {
					object = new JSONObject(str);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ContactSocket.getInstance().connect();
            	ContactSocket.getInstance().send(
            		object, new RespondListener() {
							
					@Override
					public void onSuccess(int cmd, JSONObject object) {				     
		                startActivity(new Intent(LoginPhoneActivity.this, LoginUIDActivity.class));
		                finish();
						
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
}
