
package com.jiec.contact.model;

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
