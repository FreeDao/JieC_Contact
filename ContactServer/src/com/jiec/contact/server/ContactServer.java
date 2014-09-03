/**
 * 这是qq服务器，它在监听，等待某个qq客户端，来连接
 */
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
	
	public ContactServer()
	{	
		ServerSocket ss = null;
		try {	
			//在9999监听
			System.out.println("启动服务器，端口9999");
			ss=new ServerSocket(9999);
			//阻塞,等待连接
			while(true)
			{
				Socket s=ss.accept();
				
				//接收客户端发来的信息.
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				String msg=(String)ois.readObject();
				
				System.out.println("recieve msg = " + msg);
				
				JSONObject object = JSONObject.fromObject(msg);
				
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				
				if(object.getInt("cmd") == Protocal.CMD_LOGIN_REQUEST)
				{
					if (LoginHelper.checkLoginPhone(object.getString("phoneNum"), 
							object.getString("passwd"))) {
						System.out.println("reply phone : " + object.getString("phoneNum"));
						//返回一个成功登陆的信息报
						JSONObject objectReply = new JSONObject();
						objectReply.put("seq", object.getInt("seq"));
						objectReply.put("result", 1);
						oos.writeObject(objectReply.toString());
					}else{
						//关闭Socket
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
						//返回一个成功登陆的信息报
						JSONObject objectReply = new JSONObject();
						objectReply.put("seq", object.getInt("seq"));
						objectReply.put("result", 1);
						oos.writeObject(objectReply.toString());
					}else{
						//关闭Socket
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
		}finally{
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
