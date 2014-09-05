
package com.jiec.contact.model;

public class UserModel {
    private static UserModel sInstance = null;

    private String mUserId = "1000";

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
    }

    public String getUserId() {
        return mUserId;
    }
}
