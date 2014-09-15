
package com.jiec.contact.model;

public class UserModel {
    private static UserModel sInstance = null;

    private String mUserId = "999999";

    private boolean mLogined = false;

    public void setLogined(boolean logined) {
        mLogined = logined;
        if (!mLogined) {
            mUserId = "999999";
        }
    }

    public boolean isLogined() {
        return mLogined;
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
        setLogined(true);
    }

    public String getUserId() {
        return mUserId;
    }
}
