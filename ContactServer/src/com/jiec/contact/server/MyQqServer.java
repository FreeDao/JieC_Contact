/**
 * ����qq�����������ڼ������ȴ�ĳ��qq�ͻ��ˣ�������
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
			//��9999����
			System.out.println("���Ƿ���������9999����");
			ServerSocket ss=new ServerSocket(9999);
			//����,�ȴ�����
			while(true)
			{
				Socket s=ss.accept();
				
				//���տͻ��˷�������Ϣ.
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				String msg=(String)ois.readObject();
				
				System.out.println("recieve msg = " + msg);
				
				JSONObject object = JSONObject.fromObject(msg);
				
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				if(object.getInt("cmd") == Protocal.CMD_LOGIN_REQUEST && 
						object.getString("userId").equals("123456"))
				{
					//����һ���ɹ���½����Ϣ��
					JSONObject objectReply = new JSONObject();
					objectReply.put("seq", object.getInt("seq"));
					oos.writeObject(objectReply.toString());
					
					//����͵���һ���̣߳��ø��߳���ÿͻ��˱���ͨѶ.
					SerConClientThread scct=new SerConClientThread(s);
					ManageClientThread.addClientThread(object.getString("userId"), scct);
					//������ÿͻ���ͨ�ŵ��߳�.
					scct.start();
				}else{
					//�ر�Socket
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
