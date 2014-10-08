
package com.jiec.contact.db;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jiec.contact.utils.LogUtil;

public class RecordHelper {
    public static JSONObject getRecords(String owner) {
        JSONObject object = new JSONObject();

        SqlHelper sh = new SqlHelper();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        String sql = "SELECT * FROM contact_record WHERE record_owner = '" + owner
                + "' and record_date = '" + date + "' ORDER BY record_time DESC;";
        ResultSet rs = sh.queryExecute(sql);

        if (rs == null) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();
        try {
            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("id", rs.getInt(1));
                o.put("name", rs.getString(2));
                o.put("num", rs.getString(3));
                o.put("date", rs.getString(4));
                o.put("time", rs.getString(5));
                o.put("info", rs.getString(6));
                o.put("state", rs.getInt(7));
                o.put("msg", rs.getString(9));
                o.put("type", rs.getInt(10));

                jsonArray.add(o);
            }

            object.put("records", jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    public static boolean insertRecord(JSONObject object) {
        boolean result = true;
        JSONArray recordsArray = object.getJSONArray("records");
        for (int i = 0; i < recordsArray.size(); i++) {
            object = recordsArray.getJSONObject(i);

            String sql = "INSERT INTO contact_record (record_name, record_num, record_date, record_time, record_state, record_owner, record_msg, record_type) values('"
                    + object.getString("name")
                    + "', '"
                    + object.getString("num")
                    + "', '"
                    + object.getString("date")
                    + "', '"
                    + object.getString("time")
                    + "', "
                    + object.getInt("state")
                    + ", '"
                    + object.getString("owner")
                    + "', '"
                    + object.getString("msg") + "', " + object.getInt("type") + ");";
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
