/**
 * ���ܣ��Ƿ�������ĳ���ͻ��˵�ͨ���߳�
 */
package com.jiec.contact.socket;

import java.io.ObjectInputStream;
import java.net.Socket;

import com.jiec.contact.model.Message;
public class SerConClientThread  extends Thread{

	Socket s;
	
	public SerConClientThread(Socket s)
	{
		//�ѷ������͸ÿͻ��˵����Ӹ���s
		this.s=s;
	}

	
	public void run()
	{
		
		while(true)
		{		
			//������߳̾Ϳ��Խ��տͻ��˵���Ϣ.
			try {
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				Message m=(Message)ois.readObject();
				
			//	System.out.println(m.getSender()+" �� "+m.getGetter()+" ˵:"+m.getCon());
				
				//�Դӿͻ���ȡ�õ���Ϣ���������жϣ�Ȼ������Ӧ�Ĵ���
				
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			
		}
		
		
	}
}
