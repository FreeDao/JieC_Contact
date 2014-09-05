
package com.jiec.contact;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ContactEditActivity extends Activity {
    private Button mBtnSave, mBtnBack;

    private EditText mLastEditUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact_detail_edit);

        Intent intent = getIntent();
        int flag = intent.getIntExtra(MyContactActivity.NEW_REQUEST_KEY, 0);

        mLastEditUser = (EditText) findViewById(R.id.et_last_edit_time);

        if (flag == MyContactActivity.REQUEST_NEW_CONTACT) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = sDateFormat.format(new java.util.Date());
            mLastEditUser.setText(date);
        }

        mBtnSave = (Button) findViewById(R.id.btn_save);
        mBtnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
}
