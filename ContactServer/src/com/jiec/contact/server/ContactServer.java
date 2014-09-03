
package com.jiec.contact.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import net.sf.json.JSONObject;

import com.jiec.contact.db.LoginHelper;
import com.jiec.contact.model.Protocal;

public class ContactServer {

    public ContactServer() {
        ServerSocket ss = null;
        try {
            System.out.println("server start˿port 9999");
            ss = new ServerSocket(9999);
            while (true) {
                Socket s = ss.accept();

                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                String msg = (String) ois.readObject();

                System.out.println("recieve msg = " + msg);

                JSONObject object = JSONObject.fromObject(msg);

                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

                if (object.getInt("cmd") == Protocal.CMD_LOGIN_REQUEST) {
                    if (LoginHelper.checkLoginPhone(object.getString("phoneNum"),
                            object.getString("passwd"))) {
                        System.out.println("reply phone : " + object.getString("phoneNum"));

                        JSONObject objectReply = new JSONObject();
                        objectReply.put("seq", object.getInt("seq"));
                        objectReply.put("result", 1);
                        oos.writeObject(objectReply.toString());
                    } else {

                        JSONObject objectReply = new JSONObject();
                        objectReply.put("seq", object.getInt("seq"));
                        objectReply.put("result", -1);
                        oos.writeObject(objectReply.toString());
                        s.close();
                    }

                } else if (object.getInt("cmd") == Protocal.CMD_LOGIN_USER_REQUEST) {
                    if (LoginHelper.checkLoginUser(object.getString("user_id"),
                            object.getString("user_passwd"))) {
                        System.out.println("reply user : " + object.getString("user_id"));

                        JSONObject objectReply = new JSONObject();
                        objectReply.put("seq", object.getInt("seq"));
                        objectReply.put("result", 1);
                        oos.writeObject(objectReply.toString());
                    } else {

                        JSONObject objectReply = new JSONObject();
                        objectReply.put("seq", object.getInt("seq"));
                        objectReply.put("result", -1);
                        oos.writeObject(objectReply.toString());
                        s.close();
                    }
                }
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
