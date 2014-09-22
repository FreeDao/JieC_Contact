
package com.jiec.contact.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.LogUtil;
import com.jiec.utils.SIMCardInfo;
import com.jiec.utils.ToastUtil;

public class UserModel {
    private static UserModel sInstance = null;

    private String mUserId = "999999";

    private boolean mPhoneLogined = false;

    private boolean mUserLogined = false;

    private String mPhoneNumber = "";

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
            mUserId = "999999";
        }
    }

    private UserModel() {

    }

    public boolean checkPhoneNumber(Context context) {
        mPhoneNumber = new SIMCardInfo(context).getNativePhoneNumber();

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

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                UserModel.getInstance().setPhoneLogined(true);
                LogUtil.e("手机号码合法");
            }

            @Override
            public void onFailed(int cmd, String reason) {
                // TODO Auto-generated method stub
                ToastUtil.showMsg(reason);
                android.os.Process.killProcess(android.os.Process.myPid());
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
    }

    public String getUserId() {
        return mUserId;
    }
}
