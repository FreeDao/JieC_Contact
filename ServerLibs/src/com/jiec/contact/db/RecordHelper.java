
package com.jiec.contact.db;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jiec.contact.utils.LogUtil;

public class RecordHelper {
    public static JSONObject getRecords(JSONObject requestObject) {
        JSONObject object = new JSONObject();

        String owner = requestObject.getString("user_id");

        SqlHelper sh = new SqlHelper();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        String sql = "SELECT * FROM contact_record WHERE record_owner = '" + owner
                + "' and record_start_time like '%" + date + "%' ORDER BY record_start_time DESC;";
        ResultSet rs = sh.queryExecute(sql);

        if (rs == null) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();
        try {
            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("id", rs.getInt(1));
                o.put("name", myTrim(rs.getString(2)));
                o.put("num", myTrim(rs.getString(3)));
                o.put("date", myTrim(rs.getString(4)));
                o.put("time", myTrim(rs.getString(4)));
                o.put("info", myTrim(rs.getString(6)));
                o.put("state", rs.getInt(7));
                o.put("msg", myTrim(rs.getString(9)));
                o.put("type", rs.getInt(10));
                o.put("system_id", myTrim(rs.getString(11)));

                jsonArray.add(o);
            }

            object.put("records", jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    private static String myTrim(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim();
        }
    }

    public static boolean insertRecord(JSONObject object) {
        boolean result = true;
        JSONArray recordsArray = object.getJSONArray("records");
        for (int i = 0; i < recordsArray.size(); i++) {
            object = recordsArray.getJSONObject(i);

            String sql = "INSERT INTO contact_record (record_owner, record_num, record_name, record_start_time, "
                    + "record_state, record_msg, record_type, record_unique_id) values('"
                    + object.getString("owner")
                    + "', '"
                    + object.getString("num")
                    + "', '"
                    + object.getString("name")
                    + "', '"
                    + object.getString("date")
                    + " "
                    + object.getString("time")
                    + "', "
                    + object.getInt("state")
                    + ", '"
                    + object.getString("msg")
                    + "', "
                    + object.getInt("type")
                    + ", '"
                    + object.getString("system_id") + "');";
            LogUtil.d(sql);
            SqlHelper sh = new SqlHelper();
            result = result & sh.upExecute(sql);
        }

        return result;

    }

    public static boolean updateRecord(JSONObject object) {
        String sql = "UPDATE contact_record SET record_info='" + object.getString("info")
                + "' WHERE record_id=" + object.getInt("id") + ";";
        LogUtil.d(sql);
        SqlHelper sh = new SqlHelper();
        return sh.upExecute(sql);
    }
}
