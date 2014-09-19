
package com.jiec.contact.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;

import com.jiec.contact.MyApplication;
import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.LogUtil;
import com.jiec.utils.PhoneUtils;
import com.jiec.utils.ToastUtil;

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

    private static List<Record> mRecords;

    private RecordModel() {
        mRecords = new ArrayList<Record>();
        requestData();

        scanSystemRecord();
    }

    public void scanSystemRecord() {
        synchronized (this) {
            // 讲本地的通讯记录加本地记录中，已经上传到远程服务器
            List<Record> list = PhoneUtils.getRecordsFromContact(MyApplication.getContext());
            if (list.size() > 0)
                pushRecordToServer(list);

            list = PhoneUtils.getSmsInPhone(MyApplication.getContext());
            if (list.size() > 0)
                pushRecordToServer(list);
        }
    }

    public void pushRecordToServer(final List<Record> list) {
        if (list != null) {
            mRecords.addAll(list);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    insertRecord(list);
                }
            }).start();

        }
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

    public void finish() {
        mRecords.clear();
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
                    object = object.optJSONObject("data");
                    JSONArray array = object.optJSONArray("records");
                    mRecords.clear();
                    for (int i = 0; i < array.length(); i++) {
                        Record record = new Record();
                        record.setId(array.getJSONObject(i).getInt("id"));
                        record.setNum(array.getJSONObject(i).getString("num"));
                        record.setName(array.getJSONObject(i).getString("name").length() < 1 ? ContactModel
                                .getInstance().getNameByPhoneNum(record.getNum()) : array
                                .getJSONObject(i).getString("name"));
                        record.setDate(array.getJSONObject(i).getString("date"));
                        record.setTime(array.getJSONObject(i).getString("time"));
                        record.setInfo(array.getJSONObject(i).getString("info"));
                        record.setState(array.getJSONObject(i).getInt("state"));
                        record.setMsg(array.getJSONObject(i).getString("msg"));
                        record.setType(array.getJSONObject(i).getInt("type"));
                        mRecords.add(record);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < mChangeListeners.size(); i++) {

                            mChangeListeners.get(i).onDataChanged();
                        }
                    }
                });

            }

            @Override
            public void onFailed(int cmd, String reason) {
                // TODO Auto-generated method stub

            }

        });
    }

    public void updateRecord(String info, int id) {
        JSONObject object = new JSONObject();
        try {
            object.put("seq", ContactSocket.getSeq());
            object.put("cmd", Protocal.CMD_UPDATE_CONTACT_RECORD_INFO);
            object.put("info", info);
            object.put("id", id);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                try {
                    for (int i = 0; i < mRecords.size(); i++) {
                        if (mRecords.get(i).getId() == object.getInt("id")) {
                            mRecords.get(i).setInfo(object.getString("info"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < mChangeListeners.size(); i++) {

                            mChangeListeners.get(i).onDataChanged();
                        }
                    }
                });

            }

            @Override
            public void onFailed(int cmd, String reason) {
                // TODO Auto-generated method stub
                ToastUtil.showMsg("更新不成功，请重试");
            }

        });
    }

    public void insertRecord(List<Record> records) {
        JSONObject object = new JSONObject();
        try {
            object.put("seq", ContactSocket.getSeq());
            object.put("cmd", Protocal.CMD_INSERT_CONTACT_RECORD);
            object.put("id", UserModel.getInstance().getUserId());
            JSONArray recordArray = new JSONArray();
            for (int i = 0; i < records.size(); i++) {
                JSONObject recordJsonObject = new JSONObject();
                recordJsonObject.put("name", records.get(i).getName());
                recordJsonObject.put("num", records.get(i).getNum());
                recordJsonObject.put("state", records.get(i).getState());
                recordJsonObject.put("date", records.get(i).getDate());
                recordJsonObject.put("time", records.get(i).getTime());
                recordJsonObject.put("owner", UserModel.getInstance().getUserId());
                recordJsonObject.put("msg", records.get(i).getMsg());
                recordJsonObject.put("type", records.get(i).getType());
                recordArray.put(recordJsonObject);
            }

            object.put("records", recordArray);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                try {
                    LogUtil.d("数据上传成功" + object.toString());
                    requestData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < mChangeListeners.size(); i++) {

                            mChangeListeners.get(i).onDataChanged();
                        }
                    }
                });

            }

            @Override
            public void onFailed(int cmd, String reason) {
                ToastUtil.showMsg(reason);
            }
        });
    }

    public void refreshRecord() {
        for (int i = 0; i < mRecords.size(); i++) {
            mRecords.get(i).setName(
                    ContactModel.getInstance().getNameByPhoneNum(mRecords.get(i).getNum()));

        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < mChangeListeners.size(); i++) {

                    mChangeListeners.get(i).onDataChanged();
                }
            }
        });
    }

}
