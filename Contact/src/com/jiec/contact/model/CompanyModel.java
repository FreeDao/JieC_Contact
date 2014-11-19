
package com.jiec.contact.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;

import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.LogUtil;
import com.jiec.utils.ToastUtil;

public class CompanyModel {

    public static interface OnCompanyDataChange {
        public void onDataChanged();
    }

    public static interface OnInsertDataResult {
        public void onSuccess(String id, String name);

        public void onFailed(String reason);
    }

    private int mConnectTime = 0;

    private static CompanyModel sInstance;

    private List<Company> mCompanies;

    private List<OnCompanyDataChange> mListeners = new ArrayList<CompanyModel.OnCompanyDataChange>();

    private CompanyModel() {
        mCompanies = new ArrayList<Company>();
    }

    public static CompanyModel getInstance() {
        if (sInstance == null) {
            sInstance = new CompanyModel();
        }

        return sInstance;
    }

    public void addListener(OnCompanyDataChange listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void removeListener(OnCompanyDataChange listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    public List<Company> getCompanies() {
        if (mCompanies.size() < 1 && mConnectTime < 5) {
            requestCompanies();
            mConnectTime++;
        }

        return mCompanies;
    }

    public String getCompanyName(String companyId) {
        if (companyId == null)
            return "";
        for (int i = 0; i < mCompanies.size(); i++) {
            if (mCompanies.get(i).id.equals(companyId)) {
                return mCompanies.get(i).name;
            }
        }

        return "";
    }

    public void requestCompanies() {
        JSONObject object = new JSONObject();
        try {
            object.put("seq", ContactSocket.getSeq());
            object.put("cmd", Protocal.CMD_GET_COMPANYS);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                try {
                    mConnectTime = 0;
                    mCompanies.clear();
                    object = object.getJSONObject("data");

                    LogUtil.e("onsuccess data = " + object.toString());
                    JSONArray companyArray = object.getJSONArray("companies");

                    LogUtil.e("company size = " + companyArray.length());

                    for (int i = 0; i < companyArray.length(); i++) {
                        JSONObject companyObject = companyArray.getJSONObject(i);
                        LogUtil.e("company = " + companyObject.toString());

                        Company company = new Company(companyObject.getString("id"), companyObject
                                .getString("name"));

                        mCompanies.add(company);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        for (int i = 0; i < mListeners.size(); i++) {
                            mListeners.get(i).onDataChanged();
                        }
                    }
                });

            }

            @Override
            public void onFailed(int cmd, String reason) {
                // TODO Auto-generated method stub
                ToastUtil.showMsg(reason);
            }
        });
    }

    public void insertCompany(String name, final OnInsertDataResult listener) {
        JSONObject object = new JSONObject();
        try {
            object.put("seq", ContactSocket.getSeq());
            object.put("cmd", Protocal.CMD_INSERT_NEW_COMPANY);
            object.put("company_name", name);
            object.put("creator", UserModel.getInstance().getUserId());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            object.put("createTime", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {

                LogUtil.e("onDataChanged");
                try {
                    mCompanies.add(new Company(object.getString("id"), object.getString("name")));
                    listener.onSuccess(object.getString("id"), object.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int cmd, String reason) {
                // TODO Auto-generated method stub
                listener.onFailed(reason);
            }
        });
    }
}
