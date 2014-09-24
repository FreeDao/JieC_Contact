
package com.jiec.contact;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jiec.utils.FTPClientUtils;

public class SaveFileService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        FTPClientUtils.updateFile();
        super.onCreate();
    }

}
