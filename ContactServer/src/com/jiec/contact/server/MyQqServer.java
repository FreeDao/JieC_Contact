/**
 * 这是qq服务器，它在监听，等待某个qq客户端，来连接
 */
package com.jiec.contact.server;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import net.sf.json.JSONObject;

import com.jiec.contact.model.Message;
import com.jiec.contact.model.Protocal;
import com.jiec.contact.model.User;
import com.jiec.contact.socket.ManageClientThread;
import com.jiec.contact.socket.SerConClientThread;
public class MyQqServer {
	
	public MyQqServer()
	{	
		try {	
			//在9999监听
			System.out.println("我是服务器，在9999监听");
			ServerSocket ss=new ServerSocket(9999);
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
				if(object.getInt("cmd") == Protocal.CMD_LOGIN_REQUEST && 
						object.getString("userId").equals("123456"))
				{
					//返回一个成功登陆的信息报
					JSONObject objectReply = new JSONObject();
					objectReply.put("seq", object.getInt("seq"));
					oos.writeObject(objectReply.toString());
					
					//这里就单开一个线程，让该线程与该客户端保持通讯.
					SerConClientThread scct=new SerConClientThread(s);
					ManageClientThread.addClientThread(object.getString("userId"), scct);
					//启动与该客户端通信的线程.
					scct.start();
				}else{
					//关闭Socket
					s.close();		
				}		
			}	
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			
		}
		
	}
	
}
