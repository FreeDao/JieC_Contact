
package com.jiec.contact.db;

import java.sql.ResultSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RecordHelper {
    public static JSONObject getRecords() {
        JSONObject object = new JSONObject();

        SqlHelper sh = new SqlHelper();
        String sql = "SELECT * FROM contact_record;";
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
                o.put("time", rs.getString(4));
                o.put("info", rs.getString(5));
                jsonArray.add(o);
            }

            object.put("records", jsonArray);

        } catch (Exception e) {

        }

        return object;
    }

    public static boolean insertRecord(JSONObject object) {
        String sql = "INSERT INTO contact_record (record_name, record_num, record_time, record_info) value('"
                + object.getString("name")
                + "', "
                + object.getString("num")
                + "', "
                + object.getString("time") + "', " + object.getString("info") + "')";
        SqlHelper sh = new SqlHelper();
        return sh.upExecute(sql);
    }

    public static boolean updateRecord(JSONObject object) {
        String sql = "UPDATE contact_record SET record_info='" + object.getString("info")
                + "' WHERE record_id=" + object.getInt("id") + ";)";
        SqlHelper sh = new SqlHelper();
        return sh.upExecute(sql);
    }
}
