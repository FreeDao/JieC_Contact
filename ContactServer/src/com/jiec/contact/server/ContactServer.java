
package com.jiec.contact.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import net.sf.json.JSONObject;

import com.jiec.contact.db.ContactHelper;
import com.jiec.contact.db.LoginHelper;
import com.jiec.contact.model.Protocal;

public class ContactServer {

    public ContactServer() {
        ServerSocket ss = null;
        try {
            System.out.println("server start  port 9999");
            ss = new ServerSocket(9999);
            while (true) {
                Socket s = ss.accept();

                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                String msg = (String) ois.readObject();

                System.out.println("request msg = " + msg);

                JSONObject requestObject = JSONObject.fromObject(msg);

                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                
                int cmd = requestObject.getInt("cmd");

                if (cmd == Protocal.CMD_LOGIN_REQUEST) {
                    if (LoginHelper.checkLoginPhone(requestObject.getString("phoneNum"),
                            requestObject.getString("passwd"))) {

                        JSONObject objectReply = new JSONObject();
                        objectReply.put("seq", requestObject.getInt("seq"));
                        objectReply.put("result", 1);
                        oos.writeObject(objectReply.toString());
                    } else {

                        JSONObject objectReply = new JSONObject();
                        objectReply.put("seq", requestObject.getInt("seq"));
                        objectReply.put("result", -1);
                        oos.writeObject(objectReply.toString());
                    }

                } else if (cmd == Protocal.CMD_LOGIN_USER_REQUEST) {
                    if (LoginHelper.checkLoginUser(requestObject.getString("user_id"),
                            requestObject.getString("user_passwd"))) {

                        JSONObject objectReply = new JSONObject();
                        objectReply.put("seq", requestObject.getInt("seq"));
                        objectReply.put("result", 1);
                        oos.writeObject(objectReply.toString());
                    } else {

                        JSONObject objectReply = new JSONObject();
                        objectReply.put("seq", requestObject.getInt("seq"));
                        objectReply.put("result", -1);
                        oos.writeObject(objectReply.toString());
                        
                    }
                } else if (cmd == Protocal.CMD_GET_CONTACT) {
                	JSONObject objectReply = new JSONObject();
                    objectReply.put("seq", requestObject.getInt("seq"));
                    objectReply.put("result", 1);
                    objectReply.put("contacts", ContactHelper.getContact(requestObject.getString("user_id")));
                    oos.writeObject(objectReply.toString());
                }
                
                s.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
