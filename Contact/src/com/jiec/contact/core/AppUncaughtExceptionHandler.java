
package com.jiec.contact.core;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.text.TextUtils;

import com.jiec.utils.LogUtil;

/**
 * 描述:异常处理类
 * 
 * @author chenys
 * @since 2013-7-22 下午3:16:07
 */
public class AppUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private Context mContext;

    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public AppUncaughtExceptionHandler(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);

        mDefaultHandler.uncaughtException(thread, ex);

    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        final String crashReport = getCrashReport(ex);
        final String exception = getExceptionType(ex);

        AppException.saveErrorLog(crashReport);

        return true;
    }

    /**
     * 获取APP崩溃异常报告
     * 
     * @param ex
     * @return
     */
    private String getCrashReport(Throwable ex) {
        StringBuffer exceptionStr = new StringBuffer();
        if (ex != null) {
            String errorStr = ex.getLocalizedMessage();
            if (TextUtils.isEmpty(errorStr)) {
                errorStr = ex.getMessage();
            }
            if (TextUtils.isEmpty(errorStr)) {
                errorStr = ex.toString();
            }
            exceptionStr.append("Exception: " + errorStr + "\n");
            StackTraceElement[] elements = ex.getStackTrace();
            if (elements != null) {
                for (int i = 0; i < elements.length; i++) {
                    exceptionStr.append(elements[i].toString() + "\n");
                }
            }
        } else {
            exceptionStr.append("no exception. Throwable is null\n");
        }
        if (LogUtil.isDebug()) {
            LogUtil.e(getClass().getSimpleName(), exceptionStr.toString());
        }

        // \n \t _ \r 替换成 [n] [t] [_] [r]
        return exceptionStr.toString().replace("\n", "[n]").replace("\t", "[t]")
                .replace("_", "[_]").replace("\r", "[r]");
    }

    private String getExceptionType(Throwable ex) {
        StringBuffer exceptionStr = new StringBuffer();
        if (ex != null) {
            String type = ex.toString();
            if (!TextUtils.isEmpty(type)) {
                if (type.contains(":")) {
                    int index = type.indexOf(":");
                    exceptionStr.append(type.substring(0, index));
                } else {
                    exceptionStr.append(type);
                }
            }
        }
        exceptionStr.append("");
        return exceptionStr.toString().replace(" ", "");
    }
}
