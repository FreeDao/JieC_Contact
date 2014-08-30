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

public class ContactSocket {
	
	public interface RespondListener {
		public void onSuccess(int cmd, JSONObject object);
		public void onFailed(int cmd, JSONObject object);
	}

	private static ContactSocket sIntance = null;
	
	private Socket mSocket = null;
	
	private boolean mConnected = false;
	
	private Map<Integer, RespondListener> mListeners = new HashMap<Integer, RespondListener>();
	
	public static int sSeq = 0;
	
	private ContactSocket() {
		try {
			mSocket=new Socket("192.168.0.103",9999);
			
			if (mSocket.isConnected()) {
				mConnected = true;
			}
			mThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		
		System.out.println("connect success");
	}
	
	public void send(JSONObject object, RespondListener listener) {
		try {
			Log.i("test", object.toString());
			ObjectOutputStream oos=new ObjectOutputStream(mSocket.getOutputStream());
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
	
	public Socket getSocket() {
		return mSocket;
	}
	
	public void closeSocket() {
		if (mSocket != null) {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	Thread mThread = new Thread(new Runnable() {
		
		public void run()
		{
			while(ContactSocket.this.mConnected)
			{
				//不停的读取从服务器端发来的消息
				try {
					
					ObjectInputStream ois=new ObjectInputStream(
							ContactSocket.this.mSocket.getInputStream());
					String o = (String)ois.readObject();
					JSONObject jo = new JSONObject(o);
					
					for(Entry<Integer, RespondListener> entry:mListeners.entrySet()){ 
						if (jo.getInt("seq") == entry.getKey()) {
							entry.getValue().onSuccess(entry.getKey(), jo);
							mListeners.remove(entry);
						}
					} 
					
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		}
	});
}
