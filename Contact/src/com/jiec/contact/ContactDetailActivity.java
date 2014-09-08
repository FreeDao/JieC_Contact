
package com.jiec.contact;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jiec.contact.model.Contact;
import com.jiec.contact.model.ContactModel;
import com.jiec.contact.model.ContactModel.ContactChangeListener;

public class ContactDetailActivity extends Activity implements OnClickListener,
        ContactChangeListener {

    private Button mBtnEdit, mBtnBack;

    private Contact mContact;

    ImageButton mBGCall_1, mBGCall_2, mBGCall_3, mYDCall_1, mYDCall_2, mYDCall_3;

    ImageButton mBGChat_1, mBGChat_2, mBGChat_3, mYDChat_1, mYDChat_2, mYDChat_3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact_detail);

        mContact = getIntent().getParcelableExtra("contact");

        updateView();

        mBtnEdit = (Button) findViewById(R.id.btn_edit);
        mBtnEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ContactDetailActivity.this, ContactEditActivity.class);
                intent.putExtra("contact", mContact);
                startActivity(intent);
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

        mBGCall_1 = (ImageButton) findViewById(R.id.ib_phone_call_1);
        mBGCall_1.setOnClickListener(this);
        mBGCall_2 = (ImageButton) findViewById(R.id.ib_phone_call_2);
        mBGCall_2.setOnClickListener(this);
        mBGCall_3 = (ImageButton) findViewById(R.id.ib_phone_call_3);
        mBGCall_3.setOnClickListener(this);

        mBGChat_1 = (ImageButton) findViewById(R.id.ib_phone_chat_1);
        mBGChat_1.setOnClickListener(this);
        mBGChat_2 = (ImageButton) findViewById(R.id.ib_phone_chat_2);
        mBGChat_2.setOnClickListener(this);
        mBGChat_3 = (ImageButton) findViewById(R.id.ib_phone_chat_3);
        mBGChat_3.setOnClickListener(this);

        mYDCall_1 = (ImageButton) findViewById(R.id.ib_yd_phone_call_1);
        mYDCall_1.setOnClickListener(this);
        mYDCall_2 = (ImageButton) findViewById(R.id.ib_yd_phone_call_2);
        mYDCall_2.setOnClickListener(this);
        mYDCall_3 = (ImageButton) findViewById(R.id.ib_yd_phone_call_3);
        mYDCall_3.setOnClickListener(this);

        mYDChat_1 = (ImageButton) findViewById(R.id.ib_yd_phone_chat_1);
        mYDChat_1.setOnClickListener(this);
        mYDChat_2 = (ImageButton) findViewById(R.id.ib_yd_phone_chat_2);
        mYDChat_2.setOnClickListener(this);
        mYDChat_3 = (ImageButton) findViewById(R.id.ib_yd_phone_chat_3);
        mYDChat_3.setOnClickListener(this);

        ContactModel.getInstance().addListener(this);
    }

    private void updateView() {
        TextView tvName = (TextView) findViewById(R.id.tv_username);
        tvName.setText(mContact.getName());

        TextView tv_company_id = (TextView) findViewById(R.id.tv_company_id);
        tv_company_id.setText(mContact.getCompany_id());

        TextView tv_phone_num_1 = (TextView) findViewById(R.id.tv_phone_num_1);
        tv_phone_num_1.setText(mContact.getBgdh_1());

        TextView tv_phone_num_2 = (TextView) findViewById(R.id.tv_phone_num_2);
        tv_phone_num_2.setText(mContact.getBgdh_2());

        TextView tv_phone_num_3 = (TextView) findViewById(R.id.tv_phone_num_3);
        tv_phone_num_3.setText(mContact.getBgdh_3());

        TextView tv_yd_phone_num_1 = (TextView) findViewById(R.id.tv_yd_phone_num_1);
        tv_yd_phone_num_1.setText(mContact.getYddh_1());

        TextView tv_yd_phone_num_2 = (TextView) findViewById(R.id.tv_yd_phone_num_2);
        tv_yd_phone_num_2.setText(mContact.getYddh_2());

        TextView tv_yd_phone_num_3 = (TextView) findViewById(R.id.tv_yd_phone_num_3);
        tv_yd_phone_num_3.setText(mContact.getYddh_3());

        TextView tv_qq = (TextView) findViewById(R.id.tv_qq);
        tv_qq.setText(mContact.getQq());

        TextView tv_email_1 = (TextView) findViewById(R.id.tv_email_1);
        tv_email_1.setText(mContact.getEmail_1());

        TextView tv_email_2 = (TextView) findViewById(R.id.tv_email_2);
        tv_email_2.setText(mContact.getEmail_2());

        TextView tv_email_3 = (TextView) findViewById(R.id.tv_email_3);
        tv_email_3.setText(mContact.getEmail_3());

        TextView tv_edit_user = (TextView) findViewById(R.id.tv_edit_user);
        tv_edit_user.setText(mContact.getEdit_user_id());

        TextView tv_last_time = (TextView) findViewById(R.id.tv_last_time);
        tv_last_time.setText(mContact.getLast_edit_time());

    }

    @Override
    public void onClick(View view) {
        if (view == mBGCall_1) {
            callPhone(mContact.getBgdh_1());
        } else if (view == mBGCall_2) {
            callPhone(mContact.getBgdh_2());
        } else if (view == mBGCall_3) {
            callPhone(mContact.getBgdh_3());
        } else if (view == mBGChat_1) {
            sendSMS(mContact.getBgdh_1());
        } else if (view == mBGChat_2) {
            sendSMS(mContact.getBgdh_2());
        } else if (view == mBGChat_3) {
            sendSMS(mContact.getBgdh_3());
        } else if (view == mYDCall_1) {
            callPhone(mContact.getYddh_1());
        } else if (view == mYDCall_2) {
            callPhone(mContact.getYddh_2());
        } else if (view == mYDCall_3) {
            callPhone(mContact.getYddh_3());
        } else if (view == mYDChat_1) {
            sendSMS(mContact.getYddh_1());
        } else if (view == mYDChat_2) {
            sendSMS(mContact.getYddh_2());
        } else if (view == mYDChat_3) {
            sendSMS(mContact.getYddh_3());
        }
    }

    private void callPhone(String number) {
        if (number == null || number.length() < 1) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void sendSMS(String number) {
        if (number == null || number.length() < 1) {
            return;
        }
        Uri smsToUri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", "");
        startActivity(intent);

    }

    @Override
    public void onDataChanged() {
        // TODO Auto-generated method stub
        if (ContactModel.getInstance().getContactById(mContact.getId()) != null) {
            mContact = ContactModel.getInstance().getContactById(mContact.getId());
            updateView();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ContactModel.getInstance().removeListener(this);
    }
}
