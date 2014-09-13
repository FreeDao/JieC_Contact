
package com.jiec.contact.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;

import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.ToastUtil;

public class JobModel {

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

    private static JobModel sInstance = null;

    private static List<Job> mJobs;

    private JobModel() {
        mJobs = new ArrayList<Job>();
        requestData();
    }

    public List<Job> getJobs() {
        return mJobs;
    }

    public static JobModel getInstance() {
        if (sInstance == null) {
            sInstance = new JobModel();
        }
        return sInstance;
    }

    public void requestData() {
        JSONObject object = new JSONObject();
        try {
            object.put("seq", ContactSocket.getSeq());
            object.put("cmd", Protocal.CMD_GET_JOB);
            object.put("user_id", UserModel.getInstance().getUserId());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                try {
                    object = object.getJSONObject("data");
                    JSONArray array = object.getJSONArray("jobs");
                    mJobs.clear();
                    for (int i = 0; i < array.length(); i++) {
                        Job job = new Job();
                        job.setId(array.getJSONObject(i).getInt("id"));
                        job.setName(array.getJSONObject(i).getString("name"));
                        job.setInfo(array.getJSONObject(i).getString("info"));
                        job.setOwner(array.getJSONObject(i).getString("owner"));
                        job.setCreateTime(array.getJSONObject(i).getString("create_time"));
                        job.setFinishTime(array.getJSONObject(i).getString("finish_time"));
                        mJobs.add(job);
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

    public void updateJob(String info, int id, String finishTime) {
        JSONObject object = new JSONObject();
        try {
            object.put("seq", ContactSocket.getSeq());
            object.put("cmd", Protocal.CMD_UPDATE_JOB);
            object.put("info", info);
            object.put("finish_time", finishTime);
            object.put("id", id);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                try {
                    for (int i = 0; i < mJobs.size(); i++) {
                        if (mJobs.get(i).getId() == object.getInt("id")) {
                            mJobs.get(i).setInfo(object.getString("info"));
                            mJobs.get(i).setFinishTime(object.getString("finish_time"));
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
}
