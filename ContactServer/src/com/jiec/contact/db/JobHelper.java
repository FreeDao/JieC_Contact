
package com.jiec.contact.db;

import java.sql.ResultSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jiec.contact.utils.LogUtil;

public class JobHelper {
    public static JSONObject getRecords(String owner) {
        JSONObject object = new JSONObject();

        SqlHelper sh = new SqlHelper();
        String sql = "select * from job where job_owner='" + owner + "';";
        LogUtil.d(sql);
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
                o.put("owner", rs.getString(3));
                o.put("create_time", rs.getString(4));
                o.put("finish_time", rs.getString(5));
                o.put("info", rs.getString(6));

                jsonArray.add(o);
            }

            object.put("jobs", jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.d(object.toString());
        return object;
    }

    public static boolean updateRecord(JSONObject object) {
        String sql = "UPDATE job SET job_info='" + object.getString("info")
                + "' ,job_finish_time = '" + object.getString("finish_time") + "' WHERE job_id="
                + object.getInt("id") + ";";
        LogUtil.d(sql);
        SqlHelper sh = new SqlHelper();
        return sh.upExecute(sql);
    }
}
