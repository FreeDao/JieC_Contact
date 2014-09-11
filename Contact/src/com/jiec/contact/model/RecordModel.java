
package com.jiec.contact.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;

public class RecordModel {

    public static interface OnDataChangeListener {
        public void onDataChanged();
    }

    private List<OnDataChangeListener> mChangeListeners = new ArrayList<OnDataChangeListener>();

    public void addListener(OnDataChangeListener changeListener) {
        if (!mChangeListeners.contains(changeListener))
            mChangeListeners.add(changeListener);
    }

    public void removeListener(OnDataChangeListener listener) {
        if (mChangeListeners.contains(listener)) {
            mChangeListeners.remove(listener);
        }
    }

    private static RecordModel sInstance = null;

    private List<Record> mRecords;

    private RecordModel() {
        mRecords = new ArrayList<Record>();
    }

    public List<Record> getRecords() {
        return mRecords;
    }

    public static RecordModel getInstance() {
        if (sInstance == null) {
            sInstance = new RecordModel();
        }
        return sInstance;
    }

    public void requestData() {
        JSONObject object = new JSONObject();
        try {
            object.put("seq", ContactSocket.getSeq());
            object.put("cmd", Protocal.CMD_GET_RECORD);
            object.put("user_id", UserModel.getInstance().getUserId());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                try {
                    object = object.getJSONObject("data");
                    JSONArray array = object.getJSONArray("records");
                    mRecords.clear();
                    for (int i = 0; i < array.length(); i++) {
                        Record record = new Record();
                        record.setId(array.getJSONObject(i).getInt("id"));
                        record.setName(array.getJSONObject(i).getString("name"));
                        record.setNum(array.getJSONObject(i).getString("num"));
                        record.setTime(array.getJSONObject(i).getString("time"));
                        record.setInfo(array.getJSONObject(i).getString("info"));
                        record.setState(array.getJSONObject(i).getInt("state"));
                        mRecords.add(record);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < mChangeListeners.size(); i++) {
                    mChangeListeners.get(i).onDataChanged();
                }

            }

            @Override
            public void onFailed(int cmd, String reason) {
                // TODO Auto-generated method stub

            }

        });
    }
}
