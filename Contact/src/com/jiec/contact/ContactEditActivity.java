
package com.jiec.contact;

import java.text.SimpleDateFormat;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jiec.contact.model.ContactModel;
import com.jiec.contact.model.UserModel;
import com.jiec.contact.widget.CompanyListDialog;
import com.jiec.contact.widget.CompanyListDialog.OnCompanyItemClickListener;
import com.jiec.utils.ToastUtil;

public class ContactEditActivity extends Activity {
    private Button mBtnSave, mBtnBack;

    private EditText mCompanyEditText, mNameEditText, mBG_1EditText, mBG_2EditText, mBG_3EditText;

    private EditText mYD_1EditText, mYD_2EditText, mYD_3EditText, mQQEditText, mEmail_1EditText;

    private EditText mEmail_2EditText, mEmail_3EditText, mOwnEditText, mLastEditText;

    private String mCompanyId = "";

    private String mCompanyName = "";

    private boolean mIsNewContact = false;

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact_detail_edit);

        Intent intent = getIntent();
        String flag = intent.getStringExtra(MyContactActivity.NEW_REQUEST_KEY);

        mLastEditText = (EditText) findViewById(R.id.et_last_edit_time);

        if (flag.equals(MyContactActivity.NEW_REQUEST_KEY)) {
            mIsNewContact = true;
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = sDateFormat.format(new java.util.Date());
            mLastEditText.setText(date);
        }

        mOwnEditText = (EditText) findViewById(R.id.et_edit_user);
        mOwnEditText.setText(UserModel.getInstance().getUserId());

        mCompanyEditText = (EditText) findViewById(R.id.et_company);
        mCompanyEditText.setFocusable(false);
        mCompanyEditText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CompanyListDialog dialog = new CompanyListDialog(ContactEditActivity.this,
                        new OnCompanyItemClickListener() {

                            @Override
                            public void onClick(String name, String id) {
                                mCompanyEditText.setText(name + "  (" + id + ")");
                                mCompanyName = name;
                                mCompanyId = id;
                            }
                        });
                dialog.createDialog();
            }
        });

        mNameEditText = (EditText) findViewById(R.id.et_name);
        mBG_1EditText = (EditText) findViewById(R.id.et_phone_num_1);
        mBG_2EditText = (EditText) findViewById(R.id.et_phone_num_2);
        mBG_3EditText = (EditText) findViewById(R.id.et_phone_num_3);
        mYD_1EditText = (EditText) findViewById(R.id.et_yd_phone_num_1);
        mYD_2EditText = (EditText) findViewById(R.id.et_yd_phone_num_2);
        mYD_3EditText = (EditText) findViewById(R.id.et_yd_phone_num_3);
        mQQEditText = (EditText) findViewById(R.id.et_qq);
        mEmail_1EditText = (EditText) findViewById(R.id.et_email1);
        mEmail_2EditText = (EditText) findViewById(R.id.et_email2);
        mEmail_3EditText = (EditText) findViewById(R.id.et_email3);

        mBtnSave = (Button) findViewById(R.id.btn_save);
        mBtnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mNameEditText.getText().toString().length() < 1) {
                    ToastUtil.showMsg("名字不许为空");
                    return;
                } else if (mCompanyEditText.getText().toString().length() < 1) {
                    ToastUtil.showMsg("请选择公司名称");
                    return;
                }
                if (mIsNewContact) {
                    JSONObject contact = new JSONObject();
                    try {
                        contact.put("contact_name", mNameEditText.getText().toString());
                        contact.put("contact_bgdh_1", mBG_1EditText.getText().toString());
                        contact.put("contact_bgdh_2", mBG_2EditText.getText().toString());
                        contact.put("contact_bgdh_3", mBG_3EditText.getText().toString());
                        contact.put("contact_yddh_1", mYD_1EditText.getText().toString());
                        contact.put("contact_yddh_2", mYD_2EditText.getText().toString());
                        contact.put("contact_yddh_3", mYD_3EditText.getText().toString());
                        contact.put("contact_company_id", mCompanyId);
                        contact.put("contact_company_name", mCompanyName);
                        contact.put("contact_qq", mQQEditText.getText().toString());
                        contact.put("contact_email_1", mEmail_1EditText.getText().toString());
                        contact.put("contact_email_2", mEmail_2EditText.getText().toString());
                        contact.put("contact_email_3", mEmail_3EditText.getText().toString());
                        contact.put("contact_own_id", mOwnEditText.getText().toString());
                        contact.put("contact_last_edit_time", mLastEditText.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ContactModel.getInstance().insertNewContact(contact);
                }
                finish();
            }
        });

        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mIsNewContact) {

                }
                finish();
            }
        });
    }
}
