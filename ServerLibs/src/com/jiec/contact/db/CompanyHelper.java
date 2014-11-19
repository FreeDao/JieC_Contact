
package com.jiec.contact.db;

import java.sql.ResultSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jiec.contact.utils.LogUtil;

public class CompanyHelper {

    public static JSONObject getCompanies() {
        JSONObject object = new JSONObject();

        SqlHelper sh = new SqlHelper();
        String sql = "SELECT BH,ZZ_MC FROM Lxr;";
        ResultSet rs = sh.queryExecute(sql);

        if (rs == null) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();
        try {
            while (rs.next()) {
                JSONObject perCompany = new JSONObject();
                perCompany.put("name", rs.getString(2));
                perCompany.put("id", rs.getInt(1));
                jsonArray.add(perCompany);

            }

            object.put("companies", jsonArray);
            LogUtil.d("companies = " + object.toString());

            rs.close();
            sh.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject insertCompany(JSONObject requestObject) {
        JSONObject object = new JSONObject();

        String name = requestObject.getString("company_name");
        String creator = requestObject.getString("creator");
        String createTime = requestObject.getString("createTime");

        if (isNameExist(name)) {
            object.put("result", -1);
            object.put("reason", "新增公司名字已存在，请使用现有");
            return object;
        }

        String sql = "insert into Lxr(ZZ_MC, SJLRR, XGSJ) values('" + name + "', '" + creator
                + "', '" + createTime + "');";
        LogUtil.e("sql = " + sql);
        SqlHelper sh = new SqlHelper();

        if (sh.upExecute(sql)) {
            LogUtil.d("插入成功 name = " + name);
        } else {
            object.put("result", -1);
            object.put("reason", "数据插入失败");
            return object;
        }

        sql = "select BH from Lxr where ZZ_MC = '" + name + "';";
        ResultSet rs = sh.queryExecute(sql);

        if (rs == null) {
            return null;
        }

        try {

            if (rs.next()) {
                object.put("result", 1);
                object.put("name", name);
                object.put("id", rs.getInt(1) + "");
            }

            rs.close();
            sh.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    private static boolean isNameExist(String name) {
        String sql = "select BH from Lxr where ZZ_MC = '" + name + "';";
        SqlHelper sh = new SqlHelper();
        ResultSet rs = sh.queryExecute(sql);
        try {
            if (rs == null || !rs.next()) {
                System.out.println("rs null");
                return false;
            } else {
                rs.next();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
