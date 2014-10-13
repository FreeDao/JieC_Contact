
package com.jiec.contact;

import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jiec.contact.model.CompanyModel;
import com.jiec.contact.model.Contact;
import com.jiec.contact.model.ContactModel;
import com.jiec.contact.model.ContactModel.ContactInsertListener;
import com.jiec.contact.model.UserModel;
import com.jiec.contact.widget.CompanyListDialog;
import com.jiec.contact.widget.CompanyListDialog.OnCompanyItemClickListener;
import com.jiec.contact.widget.JiecEditText;
import com.jiec.utils.PhoneNumUtils;
import com.jiec.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 描述:
 * 
 * @author jiec
 * @since 2014-10-13 上午11:07:35
 */
public class ContactEditActivity extends Activity {
    private Button mBtnSave, mBtnBack;

    private EditText mCompanyEditText, mNameEditText;

    private JiecEditText mYD_1EditText, mYD_2EditText, mYD_3EditText, mBG_1EditText, mBG_2EditText,
            mBG_3EditText;

    private EditText mQQEditText, mEmail_1EditText;

    private EditText mEmail_2EditText, mEmail_3EditText, mOwnEditText, mLastEditText;

    private String mCompanyId = "";

    private String mCompanyName = "";

    private boolean mIsNewContact = false;

    private Contact mContact = null;

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact_detail_edit);

        Intent intent = getIntent();
        String flag = intent.getStringExtra(MyContactActivity.NEW_REQUEST_KEY);

        mLastEditText = (EditText) findViewById(R.id.et_last_edit_time);

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
                                final String _name = name;
                                final String _id = id;
                                new Handler(Looper.getMainLooper()).post(new Runnable() {

                                    @Override
                                    public void run() {
                                        mCompanyEditText.setText(_name + "  (" + _id + ")");
                                        mCompanyName = _name;
                                        mCompanyId = _id;
                                    }
                                });
                            }
                        });
                dialog.createDialog();
            }
        });

        mNameEditText = (EditText) findViewById(R.id.et_name);
        mBG_1EditText = (JiecEditText) findViewById(R.id.et_phone_num_1);
        mBG_2EditText = (JiecEditText) findViewById(R.id.et_phone_num_2);
        mBG_3EditText = (JiecEditText) findViewById(R.id.et_phone_num_3);
        mYD_1EditText = (JiecEditText) findViewById(R.id.et_yd_phone_num_1);
        mYD_2EditText = (JiecEditText) findViewById(R.id.et_yd_phone_num_2);
        mYD_3EditText = (JiecEditText) findViewById(R.id.et_yd_phone_num_3);
        mQQEditText = (EditText) findViewById(R.id.et_qq);
        mEmail_1EditText = (EditText) findViewById(R.id.et_email1);
        mEmail_2EditText = (EditText) findViewById(R.id.et_email2);
        mEmail_3EditText = (EditText) findViewById(R.id.et_email3);

        if (flag != null && flag.equals(MyContactActivity.NEW_REQUEST_KEY)) {
            mIsNewContact = true;
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = sDateFormat.format(new java.util.Date());
            mLastEditText.setText(date);

            mBG_1EditText.setText(intent.getStringExtra(MyContactActivity.NEW_CONTACT_NUMBER));
        } else {
            mContact = getIntent().getParcelableExtra("contact");
            mCompanyId = mContact.getCompany_id();
            mCompanyName = CompanyModel.getInstance().getCompanyName(mCompanyId);
            mNameEditText.setText(mContact.getName());
            mCompanyEditText.setText(mCompanyName + "  (" + mCompanyId + ")");
            mCompanyId = mContact.getCompany_id();
            mBG_1EditText.setText(PhoneNumUtils.toStarPhoneNumber(mContact.getBgdh_1()));
            mBG_1EditText.setNumber(mContact.getBgdh_1());
            mBG_2EditText.setText(PhoneNumUtils.toStarPhoneNumber(mContact.getBgdh_2()));
            mBG_2EditText.setNumber(mContact.getBgdh_2());
            mBG_3EditText.setText(PhoneNumUtils.toStarPhoneNumber(mContact.getBgdh_3()));
            mBG_3EditText.setNumber(mContact.getBgdh_3());
            mYD_1EditText.setText(PhoneNumUtils.toStarPhoneNumber(mContact.getYddh_1()));
            mYD_1EditText.setNumber(mContact.getYddh_1());
            mYD_2EditText.setText(PhoneNumUtils.toStarPhoneNumber(mContact.getYddh_2()));
            mYD_2EditText.setNumber(mContact.getYddh_2());
            mYD_3EditText.setText(PhoneNumUtils.toStarPhoneNumber(mContact.getYddh_3()));
            mYD_3EditText.setNumber(mContact.getYddh_3());
            mQQEditText.setText(mContact.getQq());
            mEmail_1EditText.setText(mContact.getEmail_1());
            mEmail_2EditText.setText(mContact.getEmail_2());
            mEmail_3EditText.setText(mContact.getEmail_3());
            mLastEditText.setText(mContact.getLast_edit_time());
        }

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

                JSONObject contact = new JSONObject();
                try {
                    contact.put("contact_name", mNameEditText.getText().toString());
                    contact.put("contact_bgdh_1", mBG_1EditText.getNumber());
                    contact.put("contact_bgdh_2", mBG_2EditText.getNumber());
                    contact.put("contact_bgdh_3", mBG_3EditText.getNumber());
                    contact.put("contact_yddh_1", mYD_1EditText.getNumber());
                    contact.put("contact_yddh_2", mYD_2EditText.getNumber());
                    contact.put("contact_yddh_3", mYD_3EditText.getNumber());
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

                if (mIsNewContact) {
                    ContactModel.getInstance().insertNewContact(contact,
                            new ContactInsertListener() {

                                @Override
                                public void insertSucceess() {
                                    setResult(MyRecordActivity.SAVE_RECORD_ITEM_NUM);
                                }

                            });

                } else {
                    try {
                        contact.put("contact_id", mContact.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ContactModel.getInstance().updateContact(contact);
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
