package com.jiec.contact.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import com.jiec.contact.LoginPhoneActivity;
import com.jiec.contact.LoginUIDActivity;
import com.jiec.contact.socket.ContactSocket;
import com.jiec.contact.socket.ContactSocket.RespondListener;
import com.jiec.utils.ToastUtil;

public class ContactModel {
	/**
	 * 联系人数据变化的接口
	 * @author Administrator
	 *
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
		if (mContacts.size() == 0) {
			requestContactData();
		}
		return mContacts;
	}
	
	private void requestContactData() {
        String str = "{seq:" + (ContactSocket.getSeq()) + ",cmd:"
                + Protocal.CMD_GET_CONTACT + ",user_id:" + "\"" + 1000 + "\""                
                + "}";
        JSONObject object = null;
        try {
            object = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ContactSocket.getInstance().send(object, new RespondListener() {

            @Override
            public void onSuccess(int cmd, JSONObject object) {
                ToastUtil.showMsg("reply object = " + object.toString());
                if (mChangeListener != null) {
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
