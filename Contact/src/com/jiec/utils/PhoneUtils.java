
package com.jiec.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.CallLog;

public class PhoneUtils {

    public static void deleteRecord(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CallLog.Calls.CONTENT_URI, null, null);
    }

}
