
package com.jiec.contact.server;

import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.jiec.contact.db.CompanyHelper;
import com.jiec.contact.db.ContactHelper;
import com.jiec.contact.db.JobHelper;
import com.jiec.contact.db.LoginHelper;
import com.jiec.contact.db.RecordHelper;
import com.jiec.contact.model.Protocal;
import com.jiec.contact.utils.LogUtil;

//负责处理连接上来的客户机，即消息处理器  
public class MinaServerHandler extends IoHandlerAdapter {

    // 客户端发送的消息到达时
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        // TODO Auto-generated method stub
        String msg = (String) message;

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        LogUtil.d("<<<<" + date + " request:" + msg);

        JSONObject requestObject = JSONObject.fromObject(msg);
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
            replyObject = CompanyHelper.insertCompany(requestObject);
            replyObject.put("seq", requestObject.getInt("seq"));
        } else if (cmd == Protocal.CMD_INSERT_NEW_CONTACT) {
            ContactHelper.insertContact(requestObject.getJSONObject("contact"), replyObject);
        } else if (cmd == Protocal.CMD_UPDATE_CONTACT) {
            ContactHelper.udpateContact(requestObject.getJSONObject("contact"), replyObject);
        } else if (cmd == Protocal.CMD_GET_RECORD) {
            replyObject.put("result", 1);
            replyObject.put("data", RecordHelper.getRecords(requestObject));
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
            replyObject.put("data", JobHelper.getRecords(requestObject.getString("user_id")));
        } else if (cmd == Protocal.CMD_UPDATE_JOB) {
            replyObject.put("result", JobHelper.updateRecord(requestObject) ? 1 : -1);
            replyObject.put("info", requestObject.getString("info"));
            replyObject.put("id", requestObject.getInt("id"));
            replyObject.put("finish_time", requestObject.getString("finish_time"));
        }

        LogUtil.d(">>>>reply:" + replyObject.toString());
        session.write(replyObject.toString());

    }

    // 一个客户端关闭时
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("one Client Disconnect");
    }

    // 一个客户端接入时
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("one Client Connection");
    }

}
