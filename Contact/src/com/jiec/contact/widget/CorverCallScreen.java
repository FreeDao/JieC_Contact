
package com.jiec.contact.widget;

import android.content.Context;
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
import com.jiec.utils.PhoneNumUtils;

public class CorverCallScreen {

    private Context mContext;

    WindowManager mWindowManager;

    WindowManager.LayoutParams mParams;

    CorverScreen mCorverScreen;

    private static CorverCallScreen sInstance;

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
            }

            TextView numberTextView = (TextView) findViewById(R.id.tv_number);
            numberTextView.setText(PhoneNumUtils.toStarPhoneNumber(str));
        }

    }
}
