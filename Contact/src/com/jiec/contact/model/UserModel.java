
package com.jiec.contact.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jiec.contact.MyApplication;
import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.SIMCardInfo;
import com.jiec.utils.ToastUtil;

public class UserModel {
    private static UserModel sInstance = null;

    private String mUserId = "888888";

    private boolean mPhoneLogined = false;

    private boolean mUserLogined = false;

    private String mPhoneNumber = "";

    private boolean mIsNotAllowBlackCall = true;

    private boolean mIsCheckingPhoneNumber = true;

    private boolean mIsManager = false;

    public boolean isManager() {
        return mIsManager;
    }

    public void setIsManager(boolean isManager) {
        this.mIsManager = isManager;
    }

    public boolean isCheckingPhoneNumber() {
        return mIsCheckingPhoneNumber;
    }

    public void setIsCheckingPhoneNumber(boolean isCheckingPhoneNumber) {
        this.mIsCheckingPhoneNumber = isCheckingPhoneNumber;
    }

    public boolean isIsNotAllowBlackCall() {
        return mIsNotAllowBlackCall;
    }

    public void setIsNotAllowBlackCall(boolean isNotAllowBlackCall) {
        SharedPreferences sPreferences = MyApplication.getContext().getSharedPreferences(
                "contact_setting", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putBoolean("is_not_allow_black_call", isNotAllowBlackCall);
        editor.commit();
        this.mIsNotAllowBlackCall = isNotAllowBlackCall;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneLogined(boolean logined) {
        mPhoneLogined = logined;
    }

    public boolean isPhoneLogined() {
        return mPhoneLogined;
    }

    public boolean isUserLogined() {
        return mUserLogined;
    }

    public void setUserLogined(boolean userLogined) {
        this.mUserLogined = userLogined;
        if (!mUserLogined) {
            mUserId = "888888";
        }
    }

    private UserModel() {
        SharedPreferences sPreferences = MyApplication.getContext().getSharedPreferences(
                "contact_setting", Activity.MODE_PRIVATE);
        mIsNotAllowBlackCall = sPreferences.getBoolean("is_not_allow_black_call", true);
    }

    public boolean checkPhoneNumber(Context context) {

        mPhoneNumber = new SIMCardInfo(context).getNativePhoneNumber();
        if (TextUtils.isEmpty(mPhoneNumber)) {
            mPhoneNumber = "13670707173";
        }

        if (mPhoneNumber == null) {
            mPhoneNumber = "";
            ToastUtil.showMsg("检测不到SIM卡，请关机插上SIM卡或者检查SIM，确认无误再开机使用");
            return false;
        }
        if (mPhoneNumber.startsWith("+86")) {
            mPhoneNumber = mPhoneNumber.substring(3);
        }

        String str = "{seq:" + (ContactSocket.getSeq()) + ",cmd:" + Protocal.CMD_LOGIN_REQUEST
                + ",phoneNum:" + "\"" + mPhoneNumber + "\"" + ",passwd:" + "\"" + "1" + "\"" + "}";
        JSONObject object = null;
        try {
            object = new JSONObject(str);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ContactSocket.getInstance().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                UserModel.getInstance().setPhoneLogined(true);
                ToastUtil.showMsg("手机号码检测通过");
                setIsCheckingPhoneNumber(false);
            }

            @Override
            public void onFailed(int cmd, String reason) {
                // TODO Auto-generated method stub
                ToastUtil.showMsg(reason);
                setIsCheckingPhoneNumber(false);
            }
        });

        return true;
    }

    public static UserModel getInstance() {
        if (sInstance == null) {
            sInstance = new UserModel();
        }

        return sInstance;
    }

    public void setUserId(String id) {
        mUserId = id;
        setUserLogined(true);
        if (mUserId.equals("8888")) {
            mIsManager = true;
        }
    }

    public String getUserId() {
        return mUserId;
    }
}
