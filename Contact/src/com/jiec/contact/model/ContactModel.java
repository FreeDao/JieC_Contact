
package com.jiec.contact.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.LogUtil;
import com.jiec.utils.ToastUtil;

public class ContactModel {
    /**
     * 联系人数据变化的接口
     * 
     * @author Administrator
     */
    public static interface ContactChangeListener {
        public void onDataChanged();
    }

    private static ContactModel sInstance = null;

    private List<Company> mContacts = null;

    private ContactChangeListener mChangeListener = null;

    private ContactModel() {
        mContacts = new ArrayList<Company>();
    }

    public void setChangeListener(ContactChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    public static ContactModel getInstance() {
        if (sInstance == null) {
            sInstance = new ContactModel();
        }
        return sInstance;
    }

    public List<Company> getContacts() {
        LogUtil.e("mContacts size = " + mContacts.size());
        if (mContacts.size() == 0) {
            requestContactData();
        }
        return mContacts;
    }

    private void requestContactData() {
        String str = "{seq:" + (ContactSocket.getSeq()) + ",cmd:" + Protocal.CMD_GET_CONTACT
                + ",user_id:" + "\"" + 1000 + "\"" + "}";
        JSONObject object = null;
        try {
            object = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {

                try {
                    LogUtil.e("onsuccess data = " + object.toString());
                    JSONArray companyArray = object.getJSONArray("data");

                    LogUtil.e("company size = " + companyArray.length());

                    for (int i = 0; i < companyArray.length(); i++) {
                        JSONObject companyObject = companyArray.getJSONObject(i);
                        LogUtil.e("company = " + companyObject.toString());

                        Company company = new Company(companyObject.getString("company_id"),
                                companyObject.getString("company_id"));
                        JSONArray contactArray = companyObject.getJSONArray("company_contacts");

                        for (int j = 0; j < contactArray.length(); j++) {
                            JSONObject contactObject = contactArray.getJSONObject(j);
                            LogUtil.e("contact = " + contactObject.toString());
                            Contact contact = new Contact();
                            contact.setId(contactObject.getInt("contact_id"));
                            contact.name = contactObject.getString("contact_name");
                            contact.bgdh_1 = contactObject.getString("contact_bgdh_1");
                            contact.bgdh_2 = contactObject.getString("contact_bgdh_2");
                            contact.bgdh_3 = contactObject.getString("contact_bgdh_3");
                            contact.yddh_1 = contactObject.getString("contact_yddh_1");
                            contact.yddh_2 = contactObject.getString("contact_yddh_2");
                            contact.yddh_3 = contactObject.getString("contact_yddh_3");
                            contact.company_id = contactObject.getString("contact_company_id");
                            contact.qq = contactObject.getString("contact_qq");
                            contact.msn = contactObject.getString("contact_msn");
                            contact.email_1 = contactObject.getString("contact_email_1");
                            contact.email_2 = contactObject.getString("contact_email_2");
                            contact.email_3 = contactObject.getString("contact_email_3");
                            contact.edit_user_id = contactObject.getString("contact_edit_user_id");
                            contact.last_edit_time = contactObject
                                    .getString("contact_last_edit_time");
                            company.getContacts().add(contact);
                        }

                        mContacts.add(company);
                        LogUtil.e("add company = " + company.name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (mChangeListener != null) {
                    LogUtil.e("onDataChanged");
                    mChangeListener.onDataChanged();
                }
            }

            @Override
            public void onFailed(int cmd, String reason) {
                // TODO Auto-generated method stub
                ToastUtil.showMsg(reason);
            }
        });
    }
}
