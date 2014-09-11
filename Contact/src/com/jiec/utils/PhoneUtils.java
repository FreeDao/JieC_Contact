
package com.jiec.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

public class PhoneUtils {

    public static void callPhone(Context context, String number) {
        if (number == null || number.length() < 1) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    public static void sendSMS(Context context, String number) {
        if (number == null || number.length() < 1) {
            return;
        }
        Uri smsToUri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", "");
        context.startActivity(intent);

    }

    public static void deleteContactRecord(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CallLog.Calls.CONTENT_URI, null, null);
    }

    public static void deleteSMSRecord(Context context) {
        try {
            ContentResolver CR = context.getContentResolver();
            // Query SMS
            Uri uriSms = Uri.parse("content://sms/sent");
            Cursor c = CR.query(uriSms, new String[] {
                    "_id", "thread_id"
            }, null, null, null);
            if (null != c && c.moveToFirst()) {
                do {
                    // Delete SMS
                    long threadId = c.getLong(1);
                    CR.delete(Uri.parse("content://sms/conversations/" + threadId), null, null);
                    Log.d("deleteSMS", "threadId:: " + threadId);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.d("deleteSMS", "Exception:: " + e);
        }
    }
}
