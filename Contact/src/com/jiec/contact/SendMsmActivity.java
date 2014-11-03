
package com.jiec.contact;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiec.utils.PhoneNumUtils;
import com.jiec.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 描述:
 * 
 * @author jiec
 * @since 2014-10-27 下午4:46:28
 */
public class SendMsmActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_msm);

        final String numberString = getIntent().getStringExtra("number");
        TextView tvNumber = (TextView) findViewById(R.id.tv_number);
        tvNumber.setText(PhoneNumUtils.toStarPhoneNumber(numberString));

        final EditText etBodyEditText = (EditText) findViewById(R.id.et_msm_body);

        Button btnSendMsm = (Button) findViewById(R.id.btn_send_msm);
        btnSendMsm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(etBodyEditText.getText().toString())) {
                    ToastUtil.showMsg("短信内容不能为空");
                }

                send2(numberString, etBodyEditText.getText().toString());
                finish();
            }
        });
    }

    private void send2(String number, String message) {
        String SENT = "sms_sent";
        String DELIVERED = "sms_delivered";

        PendingIntent sentPI = PendingIntent.getActivity(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getActivity(this, 0, new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Log.i("====>", "Activity.RESULT_OK");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.i("====>", "RESULT_ERROR_GENERIC_FAILURE");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Log.i("====>", "RESULT_ERROR_NO_SERVICE");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Log.i("====>", "RESULT_ERROR_NULL_PDU");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Log.i("====>", "RESULT_ERROR_RADIO_OFF");
                        break;
                }
            }
        }, new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Log.i("====>", "RESULT_OK");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("=====>", "RESULT_CANCELED");
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager smsm = SmsManager.getDefault();
        smsm.sendTextMessage(number, null, message, sentPI, deliveredPI);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
