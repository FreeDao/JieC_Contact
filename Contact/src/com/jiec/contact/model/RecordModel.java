
package com.jiec.contact.model;

public class RecordModel {
    private static RecordModel sInstance = null;

    private RecordModel() {
        requestData();
    }

    public static RecordModel getInstance() {
        if (sInstance == null) {
            sInstance = new RecordModel();
        }
        return sInstance;
    }

    public void requestData() {

    }

}
