/**
 * 功能：是服务器和某个客户端的通信线程
 */
package com.jiec.contact.socket;

import java.io.ObjectInputStream;
import java.net.Socket;

import com.jiec.contact.model.Message;
public class SerConClientThread  extends Thread{

	Socket s;
	
	public SerConClientThread(Socket s)
	{
		//把服务器和该客户端的连接赋给s
		this.s=s;
	}

	
	public void run()
	{
		
		while(true)
		{		
			//这里该线程就可以接收客户端的信息.
			try {
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				Message m=(Message)ois.readObject();
				
			//	System.out.println(m.getSender()+" 给 "+m.getGetter()+" 说:"+m.getCon());
				
				//对从客户端取得的消息进行类型判断，然后做相应的处理
				
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			
		}
		
		
	}
}
