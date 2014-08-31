/**
 * ����qq�����������ڼ������ȴ�ĳ��qq�ͻ��ˣ�������
 */
package com.jiec.contact.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import net.sf.json.JSONObject;

import com.jiec.contact.model.Protocal;
public class ContactServer {
	
	public ContactServer()
	{	
		ServerSocket ss = null;
		try {	
			//��9999����
			System.out.println("�������������˿�9999");
			ss=new ServerSocket(9999);
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
						object.getString("passwd").equals("123456"))
				{
					System.out.println("reply user : " + object.getString("phoneNum"));
					//����һ���ɹ���½����Ϣ��
					JSONObject objectReply = new JSONObject();
					objectReply.put("seq", object.getInt("seq"));
					oos.writeObject(objectReply.toString());
					
				}else{
					//�ر�Socket
					s.close();		
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
