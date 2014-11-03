
package com.jiec.contact.db;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import com.jiec.contact.utils.LogUtil;

public class LoginHelper {

    public static String sPhone = "";

    public static String sUser = "";

    public static int sTimers = 0;

    public static boolean checkLoginPhone(String phoneNum, String passwd) {
        sPhone = phoneNum;
        return checkPasswd("SELECT PhoneIdNum FROM App_Check where PhoneIdNum = " + phoneNum,
                phoneNum);
    }

    public static boolean checkLoginUser(String userName, String passwd) {
        sUser = userName;
        if (checkPasswd("select MM from Czz where CZZBH=" + userName, passwd)) {
            insertLoginInfo();
            return true;
        }
        return false;
    }

    private static boolean checkPasswd(String sql, String passwd) {
        SqlHelper sh = new SqlHelper();
        ResultSet rs = sh.queryExecute(sql);

        try {
            if (rs != null && rs.next()) {
                String dbPasswd = rs.getString(1);

                rs.close();
                sh.close();

                if (passwd.equals(dbPasswd)) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    private static void insertLoginInfo() {
        SqlHelper sh = new SqlHelper();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = sDateFormat.format(new java.util.Date());
        String sql = "INSERT INTO App_Login (Login_Phone_Num ,Login_Phone_ID,Login_Time"
                + ",Logout_Time,Login_User,Login_Type)  " + "VALUES ('" + sPhone + "','1','" + date
                + "','','" + sUser + "','S')";

        if (sh.upExecute(sql)) {
            LogUtil.e("+++++++++++++++++++ user :" + sUser + "logined");
        } else {
            sTimers++;
            insertLoginInfo();
            if (sTimers > 3)
                return;
        }
    }
}
