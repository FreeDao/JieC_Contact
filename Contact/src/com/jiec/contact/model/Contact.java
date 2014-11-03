
package com.jiec.contact.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class Contact implements Parcelable {
    int id = 0;

    String name = "";

    String bgdh_1 = "";

    String bgdh_2 = "";

    String bgdh_3 = "";

    String yddh_1 = "";

    String yddh_2 = "";

    String yddh_3 = "";

    String company_id = "";

    String company_name = "";

    String qq = "";

    String msn = "";

    String email_1 = "";

    String email_2 = "";

    String email_3 = "";

    String own_user_id = "";

    String edit_user_id = "";

    String last_edit_time = "";

    int type = ContactType.sCustomer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBgdh_1() {
        return bgdh_1;
    }

    public void setBgdh_1(String bgdh_1) {
        this.bgdh_1 = bgdh_1;
    }

    public String getBgdh_2() {
        return bgdh_2;
    }

    public void setBgdh_2(String bgdh_2) {
        this.bgdh_2 = bgdh_2;
    }

    public String getBgdh_3() {
        return bgdh_3;
    }

    public void setBgdh_3(String bgdh_3) {
        this.bgdh_3 = bgdh_3;
    }

    public String getYddh_1() {
        return yddh_1;
    }

    public void setYddh_1(String yddh_1) {
        this.yddh_1 = yddh_1;
    }

    public String getYddh_2() {
        return yddh_2;
    }

    public void setYddh_2(String yddh_2) {
        this.yddh_2 = yddh_2;
    }

    public String getYddh_3() {
        return yddh_3;
    }

    public void setYddh_3(String yddh_3) {
        this.yddh_3 = yddh_3;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getEmail_1() {
        return email_1;
    }

    public void setEmail_1(String email_1) {
        this.email_1 = email_1;
    }

    public String getEmail_2() {
        return email_2;
    }

    public void setEmail_2(String email_2) {
        this.email_2 = email_2;
    }

    public String getEmail_3() {
        return email_3;
    }

    public void setEmail_3(String email_3) {
        this.email_3 = email_3;
    }

    public String getOwn_user_id() {
        return own_user_id;
    }

    public void setOwn_user_id(String own_user_id) {
        this.own_user_id = own_user_id;
    }

    public String getEdit_user_id() {
        return edit_user_id;
    }

    public void setEdit_user_id(String edit_user_id) {
        this.edit_user_id = edit_user_id;
    }

    public String getLast_edit_time() {
        return last_edit_time;
    }

    public void setLast_edit_time(String last_edit_time) {
        this.last_edit_time = last_edit_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(bgdh_1);
        parcel.writeString(bgdh_2);
        parcel.writeString(bgdh_3);
        parcel.writeString(yddh_1);
        parcel.writeString(yddh_2);
        parcel.writeString(yddh_3);
        parcel.writeString(company_id);
        parcel.writeString(qq);
        parcel.writeString(msn);
        parcel.writeString(email_1);
        parcel.writeString(email_2);
        parcel.writeString(email_3);
        parcel.writeString(own_user_id);
        parcel.writeString(edit_user_id);
        parcel.writeString(last_edit_time);
        parcel.writeInt(type);
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Creator<Contact>() {

        @Override
        public Contact createFromParcel(Parcel parcel) {
            Contact contact = new Contact();
            contact.id = parcel.readInt();
            contact.name = parcel.readString();
            contact.bgdh_1 = parcel.readString();
            contact.bgdh_2 = parcel.readString();
            contact.bgdh_3 = parcel.readString();
            contact.yddh_1 = parcel.readString();
            contact.yddh_2 = parcel.readString();
            contact.yddh_3 = parcel.readString();
            contact.company_id = parcel.readString();
            contact.qq = parcel.readString();
            contact.msn = parcel.readString();
            contact.email_1 = parcel.readString();
            contact.email_2 = parcel.readString();
            contact.email_3 = parcel.readString();
            contact.own_user_id = parcel.readString();
            contact.edit_user_id = parcel.readString();
            contact.last_edit_time = parcel.readString();
            contact.type = parcel.readInt();

            return contact;
        }

        @Override
        public Contact[] newArray(int arg0) {
            // TODO Auto-generated method stub
            return new Contact[arg0];
        }
    };

    public void setContact(Contact c) {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
