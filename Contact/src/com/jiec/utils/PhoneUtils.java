
package com.jiec.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import com.jiec.contact.SendMsmActivity;
import com.jiec.contact.model.ContactModel;
import com.jiec.contact.model.Record;

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
        // Uri smsToUri = Uri.parse("smsto:" + number);
        // Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        // intent.putExtra("sms_body", "");
        // context.startActivity(intent);

        Intent intent = new Intent(context, SendMsmActivity.class);
        intent.putExtra("number", number);
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
            Uri uriSms = Uri.parse("content://sms/");
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

    public static List<Record> getSmsInPhone(Context context) {
        final String SMS_URI_ALL = "content://sms/";
        List<Record> records = new ArrayList<Record>();

        try {
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[] {
                    "_id", "address", "person", "body", "date", "type"
            };
            Uri uri = Uri.parse(SMS_URI_ALL);
            Cursor cur = cr.query(uri, projection, null, null, "date desc");

            if (cur.moveToFirst()) {
                int id = cur.getColumnIndex("_id");

                int nameColumn = cur.getColumnIndex("person");
                int phoneNumberColumn = cur.getColumnIndex("address");
                int smsbodyColumn = cur.getColumnIndex("body");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");

                do {
                    Record record = new Record();

                    record.setNum(PhoneNumUtils.standard(cur.getString(phoneNumberColumn)));
                    record.setName(cur.getString(nameColumn) == null ? ContactModel.getInstance()
                            .getNameByPhoneNum(cur.getString(phoneNumberColumn)) : cur
                            .getString(nameColumn));
                    record.setMsg(cur.getString(smsbodyColumn) == null ? "" : cur
                            .getString(smsbodyColumn));
                    record.setInfo("");
                    record.setType(1);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                    record.setDate(dateFormat.format(d));

                    // 插入今天的数据
                    if (!dateFormat.format(d).equals(dateFormat.format(new Date()))) {
                        continue;
                    }

                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    record.setTime(dateFormat.format(new Date(Long.parseLong(cur
                            .getString(dateColumn)))));

                    int typeId = cur.getInt(typeColumn);
                    record.setState(typeId);
                    record.setSystem_id(record.getDate() + "_" + cur.getInt(id));
                    records.add(record);

                } while (cur.moveToNext());
            } else {
                return records;
            }

        } catch (SQLiteException ex) {
            ex.printStackTrace();

        }

        deleteSMSRecord(context);
        return records;
    }

    public static List<Record> getRecordsFromContact(Context context) {
        List<Record> records = new ArrayList<Record>();
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null,
                null, null);

        if (cursor.getCount() <= 0) {
            return records;
        }

        cursor.moveToFirst();
        do {
            Record record = new Record();

            /* Reading Date */
            SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss");
            String time = sfd.format(new Date(cursor.getLong(cursor
                    .getColumnIndex(CallLog.Calls.DATE))));
            sfd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sfd.format(new Date(cursor.getLong(cursor
                    .getColumnIndex(CallLog.Calls.DATE))));
            record.setDate(date);
            record.setTime(time);
            record.setMsg("");
            record.setType(0);

            /* Reading duration */
            // call.duration =
            // cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));

            /* Reading Date */
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            record.setState(type);

            record.setNum(PhoneNumUtils.standard(cursor.getString(cursor
                    .getColumnIndex(CallLog.Calls.NUMBER))));

            record.setName(ContactModel.getInstance().getNameByPhoneNum(record.getNum()));
            record.setSystem_id(date);

            records.add(record);
        } while (cursor.moveToNext());

        deleteContactRecord(context);

        return records;
    }
}
