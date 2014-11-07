
package com.jiec.contact.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiec.contact.MyApplication;
import com.jiec.contact.R;
import com.jiec.contact.model.CompanyModel;
import com.jiec.contact.model.Contact;
import com.jiec.contact.model.ContactModel;
import com.jiec.contact.model.ContactType;
import com.jiec.utils.PhoneNumUtils;

public class CorverCallScreen {

    private Context mContext;

    WindowManager mWindowManager;

    WindowManager.LayoutParams mParams;

    CorverScreen mCorverScreen;

    private static CorverCallScreen sInstance;

    private boolean mIsShow = false;

    public static CorverCallScreen getInstance() {
        if (sInstance == null) {
            sInstance = new CorverCallScreen(MyApplication.getContext());
        }

        return sInstance;
    }

    private CorverCallScreen(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;

        mWindowManager = (WindowManager) mContext.getApplicationContext()
                .getSystemService("window");

        mParams = new WindowManager.LayoutParams();
        mParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;

        mParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;

        // 调整悬浮窗口至左上角
        mParams.gravity = Gravity.LEFT | Gravity.TOP;

        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);

        // 设置悬浮窗口长宽数据
        mParams.width = dm.widthPixels;
        mParams.height = dm.heightPixels / 4;

    }

    public void addCorverScreen(final String number) {
        if (mIsShow)
            return;
        mIsShow = true;
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                mCorverScreen = new CorverScreen(mContext, number);
                mWindowManager.addView(mCorverScreen, mParams);
            }
        });

    }

    public void removeCorverScreen() {

        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                if (mCorverScreen != null) {
                    mWindowManager.removeView(mCorverScreen);
                    mIsShow = false;
                }
            }
        });

    }

    class CorverScreen extends RelativeLayout {

        public CorverScreen(Context context, String str) {
            super(context);
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(R.layout.layout_corver_screen, this);

            Contact contact = ContactModel.getInstance().getContactByNameOrPhoneNumber(str);
            if (contact != null) {

                TextView name = (TextView) findViewById(R.id.tv_name);
                name.setText(contact.getName());

                TextView company = (TextView) findViewById(R.id.tv_company);
                company.setText(CompanyModel.getInstance().getCompanyName(contact.getCompany_id())
                        + " (" + contact.getCompany_id() + ")");

                TextView numberTextView = (TextView) findViewById(R.id.tv_number);
                numberTextView.setText(PhoneNumUtils.toStarPhoneNumber(str));

                TextView typeTextView = (TextView) findViewById(R.id.tv_number_type);
                if (contact.getType() == ContactType.sCustomer) {
                    typeTextView.setText("客户电话，放心接听");
                } else if (contact.getType() == ContactType.sHarass) {
                    typeTextView.setTextColor(Color.RED);
                    typeTextView.setText("骚扰电话，请注意，建议挂断");
                } else if (contact.getType() == ContactType.sAdv) {
                    typeTextView.setTextColor(Color.RED);
                    typeTextView.setText("广告电话，建议挂断");
                } else if (contact.getType() == ContactType.sBank) {
                    typeTextView.setText("银行电话");
                } else if (contact.getType() == ContactType.sGovernment) {
                    typeTextView.setText("政府电话");
                } else if (contact.getType() == ContactType.sOthers) {
                    typeTextView.setText("未归类");
                }
            }

        }
    }
}
