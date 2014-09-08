
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

    private List<ContactChangeListener> mChangeListeners = new ArrayList<ContactModel.ContactChangeListener>();

    private ContactModel() {
        mContacts = new ArrayList<Company>();
    }

    public void addListener(ContactChangeListener changeListener) {
        if (!mChangeListeners.contains(changeListener))
            mChangeListeners.add(changeListener);
    }

    public void removeListener(ContactChangeListener listener) {
        if (mChangeListeners.contains(listener)) {
            mChangeListeners.remove(listener);
        }
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

    public Contact getContactById(int id) {
        for (int i = 0; i < mContacts.size(); i++) {
            for (int j = 0; j < mContacts.get(i).getContacts().size(); j++) {
                if (id == mContacts.get(i).getContacts().get(j).getId()) {
                    return mContacts.get(i).getContacts().get(j);
                }
            }
        }

        return null;
    }

    public void finish() {
        mContacts.clear();
    }

    private void requestContactData() {
        String str = "{seq:" + (ContactSocket.getSeq()) + ",cmd:" + Protocal.CMD_GET_CONTACT
                + ",user_id:" + "\"" + UserModel.getInstance().getUserId() + "\"" + "}";
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
                    object = object.getJSONObject("contacts");

                    LogUtil.e("onsuccess data = " + object.toString());
                    JSONArray companyArray = object.getJSONArray("data");

                    LogUtil.e("company size = " + companyArray.length());

                    for (int i = 0; i < companyArray.length(); i++) {
                        JSONObject companyObject = companyArray.getJSONObject(i);
                        LogUtil.e("company = " + companyObject.toString());

                        Company company = new Company(companyObject.getString("company_id"),
                                companyObject.getString("company_name"));

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

                for (int i = 0; i < mChangeListeners.size(); i++) {
                    mChangeListeners.get(i).onDataChanged();
                }
            }

            @Override
            public void onFailed(int cmd, String reason) {
                // TODO Auto-generated method stub
                ToastUtil.showMsg(reason);
            }
        });
    }

    public boolean insertNewContact(final JSONObject contact) {
        JSONObject object = new JSONObject();
        try {
            object.put("seq", ContactSocket.getSeq());
            object.put("cmd", Protocal.CMD_INSERT_NEW_CONTACT);
            object.put("contact", contact);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                ToastUtil.showMsg("新建联系人成功");

                try {
                    Contact c = new Contact();
                    c.id = object.getInt("contact_id");
                    LogUtil.e("id = " + c.id);
                    c.name = contact.getString("contact_name");
                    c.bgdh_1 = contact.getString("contact_bgdh_1");
                    c.bgdh_2 = contact.getString("contact_bgdh_2");
                    c.bgdh_3 = contact.getString("contact_bgdh_3");
                    c.yddh_1 = contact.getString("contact_yddh_1");
                    c.yddh_2 = contact.getString("contact_yddh_2");
                    c.yddh_3 = contact.getString("contact_yddh_3");
                    c.company_id = contact.getString("contact_company_id");
                    c.qq = contact.getString("contact_qq");
                    c.email_1 = contact.getString("contact_email_1");
                    c.email_2 = contact.getString("contact_email_2");
                    c.email_3 = contact.getString("contact_email_3");
                    c.edit_user_id = contact.getString("contact_own_id");
                    c.last_edit_time = contact.getString("contact_last_edit_time");

                    for (int i = 0; i < mContacts.size(); i++) {
                        if (mContacts.get(i).getId()
                                .equals(contact.getString("contact_company_id"))) {
                            mContacts.get(i).getContacts().add(c);
                            break;
                        } else if (i == mContacts.size() - 1) {
                            Company company = new Company(contact.getString("contact_company_id"),
                                    contact.getString("contact_company_name"));
                            company.getContacts().add(c);
                            mContacts.add(company);
                        }
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
                ToastUtil.showMsg(reason);
            }
        });

        return true;
    }

    public boolean updateContact(final JSONObject contact) {
        JSONObject object = new JSONObject();
        try {
            object.put("seq", ContactSocket.getSeq());
            object.put("cmd", Protocal.CMD_UPDATE_CONTACT);
            object.put("contact", contact);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        new ContactSocket().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                ToastUtil.showMsg("联系人更新成功");

                try {
                    object = object.getJSONObject("contact");
                    Contact c = new Contact();
                    c.id = object.getInt("contact_id");
                    c.name = object.getString("contact_name");
                    c.bgdh_1 = object.getString("contact_bgdh_1");
                    c.bgdh_2 = object.getString("contact_bgdh_2");
                    c.bgdh_3 = object.getString("contact_bgdh_3");
                    c.yddh_1 = object.getString("contact_yddh_1");
                    c.yddh_2 = object.getString("contact_yddh_2");
                    c.yddh_3 = object.getString("contact_yddh_3");
                    c.company_id = object.getString("contact_company_id");
                    c.qq = object.getString("contact_qq");
                    c.email_1 = object.getString("contact_email_1");
                    c.email_2 = object.getString("contact_email_2");
                    c.email_3 = object.getString("contact_email_3");
                    c.edit_user_id = object.getString("contact_own_id");
                    c.last_edit_time = object.getString("contact_last_edit_time");

                    boolean companyExist = false;
                    for (int i = 0; i < mContacts.size(); i++) {
                        if (mContacts.get(i).getId().equals(c.company_id)) {
                            companyExist = true;
                            break;
                        }
                    }

                    for (int i = 0; i < mContacts.size(); i++) {
                        for (int j = 0; j < mContacts.get(i).getContacts().size(); j++) {
                            if (mContacts.get(i).getContacts().get(j).getId() == c.id) {
                                if (companyExist) {
                                    mContacts.get(i).getContacts().remove(j);
                                    mContacts.get(i).getContacts().add(c);
                                } else {
                                    mContacts.remove(i);
                                    Company company = new Company(object
                                            .getString("contact_company_id"), object
                                            .getString("contact_company_name"));
                                    company.getContacts().add(c);
                                    mContacts.add(company);
                                }
                                break;
                            }
                        }
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
                ToastUtil.showMsg(reason);
            }
        });

        return true;
    }
}
