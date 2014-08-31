package com.jiec.contact.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import android.util.Log;

import com.jiec.contact.model.Message;
import com.jiec.contact.model.Protocal;
import com.jiec.contact.model.User;
import com.jiec.utils.ToastUtil;

public class ContactSocket {
	
	public interface RespondListener {
		public void onSuccess(int cmd, JSONObject object);
		public void onFailed(int cmd, JSONObject object);
	}

	private static ContactSocket sIntance = null;
	
	private static String SERVER_IP = "192.168.0.103";
	//private static String SERVER_IP = "114.215.153.4";
	
	private static int SERVER_PORT = 9999;
	
	private static Socket sSocket = null;
	
	private boolean mConnected = false;
	
	private Map<Integer, RespondListener> mListeners = new HashMap<Integer, RespondListener>();
	
	public static int sSeq = 0;
	
	
	private ContactSocket() {
	
	}
	
	public void send(JSONObject object, RespondListener listener) {
		try {
			Log.i("test", object.toString());
			ObjectOutputStream oos=new ObjectOutputStream(sSocket.getOutputStream());
			oos.writeObject(object.toString());
			
			if (listener != null) {
				mListeners.put(object.getInt("seq"), listener);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	
	public static ContactSocket getInstance() {
		if (sIntance == null) {
			sIntance = new ContactSocket();
		}
		
		return sIntance;
	}
	
	public void connect() {
		try {
			sSocket=new Socket(SERVER_IP, SERVER_PORT);
			
			if (sSocket.isConnected()) {
				mConnected = true;
			}
			mThread.start();

		} catch (Exception e) {
			ToastUtil.showMsg("本地网络出现问题或者服务器中断，请确定本地网络！如果本地正常请联系负责人");
			e.printStackTrace();
		} 
	}
	
	public Socket getSocket() {
		return sSocket;
	}
	
	public void closeSocket() {
		mConnected = false;
		if (sSocket != null) {
			try {
				sSocket.close();
				sSocket = null;
				sIntance = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	Thread mThread = new Thread(new Runnable() {
		
		public void run()
		{
			while(mConnected)
			{
				//不停的读取从服务器端发来的消息
				try {						
					if (sSocket == null || sSocket.isClosed()) {
						return;
					} else {
						ObjectInputStream ois=new ObjectInputStream(
								sSocket.getInputStream());
						String o = (String)ois.readObject();
						JSONObject jo = new JSONObject(o);
						
						for(Entry<Integer, RespondListener> entry:mListeners.entrySet()){ 
							if (jo.getInt("seq") == entry.getKey()) {
								entry.getValue().onSuccess(entry.getKey(), jo);
								mListeners.remove(entry);
							}
						}
					}
					
					Thread.sleep(100);
					
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				} 
			}
		}
	});
}
