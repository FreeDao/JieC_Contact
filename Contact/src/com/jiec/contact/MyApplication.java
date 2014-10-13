
package com.jiec.contact;

import android.app.Application;
import android.content.Context;

import com.jiec.contact.core.AppUncaughtExceptionHandler;

/**
 * 描述:
 * 
 * @author jiec
 * @since 2014-10-13 上午11:07:35
 */
public class MyApplication extends Application {

    private static Context mContext = null;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new AppUncaughtExceptionHandler(this));

        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
