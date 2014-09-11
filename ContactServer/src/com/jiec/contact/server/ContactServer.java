
package com.jiec.contact.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;

import com.jiec.contact.db.CompanyHelper;
import com.jiec.contact.db.ContactHelper;
import com.jiec.contact.db.LoginHelper;
import com.jiec.contact.db.RecordHelper;
import com.jiec.contact.model.Protocal;
import com.jiec.contact.utils.LogUtil;

public class ContactServer {

    public ContactServer() {
        ServerSocket ss = null;
        Socket s = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            System.out.println("server start at port 9999");
            ss = new ServerSocket(9999);

            while (true) {
                s = ss.accept();

                ois = new ObjectInputStream(s.getInputStream());
                String msg = (String) ois.readObject();

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sDateFormat.format(new java.util.Date());
                LogUtil.d("<<<<" + date + " request:" + msg);

                JSONObject requestObject = JSONObject.fromObject(msg);

                oos = new ObjectOutputStream(s.getOutputStream());

                int cmd = requestObject.getInt("cmd");

                JSONObject replyObject = new JSONObject();
                replyObject.put("seq", requestObject.getInt("seq"));
                if (cmd == Protocal.CMD_LOGIN_REQUEST) {
                    if (LoginHelper.checkLoginPhone(requestObject.getString("phoneNum"),
                            requestObject.getString("passwd"))) {

                        replyObject.put("result", 1);
                    } else {
                        replyObject.put("result", -1);
                        replyObject.put("reason", "密码与用户名对应不正确");
                    }

                } else if (cmd == Protocal.CMD_LOGIN_USER_REQUEST) {
                    if (LoginHelper.checkLoginUser(requestObject.getString("user_id"),
                            requestObject.getString("user_passwd"))) {

                        replyObject.put("result", 1);
                    } else {
                        replyObject.put("result", -1);
                        replyObject.put("reason", "密码与用户名对应不正确");
                    }
                } else if (cmd == Protocal.CMD_GET_CONTACT) {
                    replyObject.put("result", 1);
                    replyObject.put("contacts",
                            ContactHelper.getContact(requestObject.getString("user_id")));
                } else if (cmd == Protocal.CMD_GET_COMPANYS) {
                    replyObject.put("result", 1);
                    replyObject.put("data", CompanyHelper.getCompanies());
                } else if (cmd == Protocal.CMD_INSERT_NEW_COMPANY) {
                    replyObject = CompanyHelper.insertCompany(requestObject
                            .getString("company_name"));
                    replyObject.put("seq", requestObject.getInt("seq"));
                } else if (cmd == Protocal.CMD_INSERT_NEW_CONTACT) {
                    ContactHelper
                            .insertContact(requestObject.getJSONObject("contact"), replyObject);
                } else if (cmd == Protocal.CMD_UPDATE_CONTACT) {
                    ContactHelper
                            .udpateContact(requestObject.getJSONObject("contact"), replyObject);
                } else if (cmd == Protocal.CMD_GET_RECORD) {
                    replyObject.put("result", 1);
                    replyObject.put("data",
                            RecordHelper.getRecords(requestObject.getString("user_id")));
                } else if (cmd == Protocal.CMD_DELETE_CONTACT_RECORD) {
                    // replyObject.put("result", RecordHelper.)
                } else if (cmd == Protocal.CMD_UPDATE_CONTACT_RECORD_INFO) {
                    replyObject.put("result", RecordHelper.updateRecord(requestObject));
                } else if (cmd == Protocal.CMD_INSERT_CONTACT_RECORD) {
                    replyObject.put("result", RecordHelper.insertRecord(requestObject));
                }

                LogUtil.d(">>>>reply:" + replyObject.toString());
                oos.writeObject(replyObject.toString());

            }

        } catch (Exception e) {
            e.printStackTrace();

            try {
                Runtime.getRuntime().exec("setsid sh startServer.sh");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (ois != null)
                    ois.close();
                if (oos != null)
                    oos.close();
                if (s != null)
                    s.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }
}
