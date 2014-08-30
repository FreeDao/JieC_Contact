
package com.jiec.contact;

import com.jiec.utils.LockLayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginUIDActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_login_user_id);
        mHandler.postDelayed(mDisableHomeKeyRunnable,200);

        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(LoginUIDActivity.this, MainActivity.class));
                finish();
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

}
