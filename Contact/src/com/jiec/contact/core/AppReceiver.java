package com.jiec.contact.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.jiec.contact.MainActivity;
import com.jiec.contact.MyApplication;
import com.jiec.utils.AppUtil;
import com.jiec.utils.LogUtil;
import com.jiec.utils.ToastUtil;

public class AppReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();

		if (Intent.ACTION_NEW_OUTGOING_CALL.equals(action)) {
			LogUtil.d("去电了");
			
		} else if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
			// 开机启动
			// 启动检测已预订礼包是否可以领取的服务
			Intent mainIntent = new Intent(context, MainActivity.class);
			context.startActivity(mainIntent);
		} else {
			TelephonyManager tm = (TelephonyManager)
					context.getSystemService(Service.TELEPHONY_SERVICE);  
			tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	PhoneStateListener listener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				LogUtil.d("挂断");
				
				Intent intent = new Intent(
						MyApplication.getContext(), MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				MyApplication.getContext().startActivity(intent);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				LogUtil.d("接听");
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				LogUtil.d("响铃:来电号码" + incomingNumber);
				// 输出来电号码
				break;
			}
		}
	};

}
