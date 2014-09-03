
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

import com.jiec.contact.model.Protocal;
import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.ToastUtil;

public class LoginUIDActivity extends Activity {

	private EditText mUserId, mUserPasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_user_id);
        
        mUserId = (EditText) findViewById(R.id.editText2);
        mUserPasswd = (EditText) findViewById(R.id.editText1);
        
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
            		
            	
            	String str = "{seq:" + (ContactSocket.sSeq++) + 
            			",cmd:" + Protocal.CMD_LOGIN_USER_REQUEST + 
            			",user_id:" + "\"" + mUserId.getText() + "\"" + 
            			",user_passwd:" + "\"" + mUserPasswd.getText() +  "\"" + 
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
						startActivity(new Intent(LoginUIDActivity.this, MainActivity.class));
		                finish();;
						
					}
					
					@Override
					public void onFailed(int cmd, String reason) {
						ToastUtil.showMsg(reason);
					}
				});
            	
                
            }
        });
    }  

}
