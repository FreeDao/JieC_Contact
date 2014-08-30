
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

import com.jiec.contact.model.Message;
import com.jiec.contact.model.Protocal;
import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;

public class LoginPhoneActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_phone);
        
        mHandler.postDelayed(mDisableHomeKeyRunnable,200);

        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	String str = "{seq:" + (ContactSocket.sSeq++) + 
            			",cmd:" + Protocal.CMD_LOGIN_REQUEST + 
            			",userId:" + "\"123456\"" + 
            			",passwd:" + "\"123456\"" + "}";
            	JSONObject object = null;
				try {
					object = new JSONObject(str);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	ContactSocket.getInstance().send(
            		object, new RespondListener() {
							
					@Override
					public void onSuccess(int cmd, JSONObject object) {				     
		                startActivity(new Intent(LoginPhoneActivity.this, LoginUIDActivity.class));
		                finish();
						
					}
					
					@Override
					public void onFailed(int cmd, JSONObject object) {
						// TODO Auto-generated method stub
						
					}
				});
            }
        });
        
        
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
    	if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_MENU) {
            //监控/拦截菜单键
        	return true;
        } else if(keyCode == KeyEvent.KEYCODE_HOME) {
        	return true;
            //由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
        }
    	return super.dispatchKeyEvent(event);
    }
}
