
package com.jiec.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

public class ConfigUtils {
    private static final String TAG = "ConfigUtils";

    private static String sServerIp = null;

    private static int sServerPort = -1;

    private static String sFtpIp = null;

    public static String getsServerIp(Context context) {
        if (sServerIp == null) {
            sServerIp = getMetaString(context, "SERVER_IP");
        }
        return sServerIp;
    }

    public static int getsServerPort(Context context) {
        if (sServerPort == -1) {
            sServerPort = getMetaInt(context, "SERVER_PORT");
        }
        return sServerPort;
    }

    public static String getsFtpIp(Context context) {
        if (sFtpIp == null) {
            sFtpIp = getMetaString(context, "FTP_IP");
        }
        return sFtpIp;
    }

    private static String getMetaString(Context context, String name) {
        String msg = getMeta(context).getString(name);
        Log.d(TAG, " msg == " + msg);
        return msg;
    }

    private static int getMetaInt(Context context, String name) {
        int msg = getMeta(context).getInt(name);
        Log.d(TAG, " msg == " + msg);
        return msg;
    }

    private static Bundle getMeta(Context context) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return appInfo.metaData;
    }
}
