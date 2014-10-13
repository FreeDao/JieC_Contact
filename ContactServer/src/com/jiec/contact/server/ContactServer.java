
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
import com.jiec.contact.db.JobHelper;
import com.jiec.contact.db.LoginHelper;
import com.jiec.contact.db.RecordHelper;
import com.jiec.contact.model.Protocal;
import com.jiec.contact.utils.AppException;
import com.jiec.contact.utils.LogUtil;

public class ContactServer {

    public ContactServer() {
        ServerSocket ss = null;
        Socket socket = null;
        try {
            System.out.println("server start at port 9090");
            ss = new ServerSocket(9090);

            while (true) {
                socket = ss.accept();
                new DoSocketWork(socket).start();
            }

        } catch (Exception e) {
            AppException.run(e);

            try {
                Runtime.getRuntime().exec("setsid sh startServer.sh");
            } catch (IOException e1) {
                AppException.run(e1);
            }
        }

    }

    class DoSocketWork extends Thread {
        Socket s = null;

        public DoSocketWork(Socket socket) {
            s = socket;
            // TODO Auto-generated constructor stub
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                String msg = (String) ois.readObject();

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sDateFormat.format(new java.util.Date());
                LogUtil.d("<<<<" + date + " request:" + msg);

                JSONObject requestObject = JSONObject.fromObject(msg);

                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

                int cmd = requestObject.getInt("cmd");

                JSONObject replyObject = new JSONObject();
                replyObject.put("seq", requestObject.getInt("seq"));
                replyObject.put("reason", "");
                if (cmd == Protocal.CMD_LOGIN_REQUEST) {
                    if (LoginHelper.checkLoginPhone(requestObject.getString("phoneNum"),
                            requestObject.getString("passwd"))) {

                        replyObject.put("result", 1);
                    } else {
                        replyObject.put("result", -1);
                        replyObject.put("reason", "手机用户名不存在，请后台添加手机号码或者更换手机卡");
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
                    replyObject.put("result", RecordHelper.updateRecord(requestObject) ? 1 : -1);
                    replyObject.put("info", requestObject.getString("info"));
                    replyObject.put("id", requestObject.getInt("id"));
                } else if (cmd == Protocal.CMD_INSERT_CONTACT_RECORD) {
                    replyObject.put("result", RecordHelper.insertRecord(requestObject) ? 1 : -1);
                } else if (cmd == Protocal.CMD_GET_JOB) {
                    replyObject.put("result", 1);
                    replyObject.put("data",
                            JobHelper.getRecords(requestObject.getString("user_id")));
                } else if (cmd == Protocal.CMD_UPDATE_JOB) {
                    replyObject.put("result", JobHelper.updateRecord(requestObject) ? 1 : -1);
                    replyObject.put("info", requestObject.getString("info"));
                    replyObject.put("id", requestObject.getInt("id"));
                    replyObject.put("finish_time", requestObject.getString("finish_time"));
                }

                LogUtil.d(">>>>reply:" + replyObject.toString());
                oos.writeObject(replyObject.toString());

                try {
                    if (ois != null)
                        ois.close();
                    if (oos != null)
                        oos.close();
                    if (s != null)
                        s.close();
                } catch (Exception e2) {
                    AppException.run(e2);
                }

            } catch (Exception e) {
                AppException.run(e);
            }

            super.run();
        }
    }
}
