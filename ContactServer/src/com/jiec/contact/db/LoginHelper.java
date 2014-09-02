
package com.jiec.contact.db;

import java.sql.ResultSet;

import org.apache.commons.logging.Log;

public class LoginHelper {

    public static boolean checkLoginPhone(String phoneNum, String passwd) {

        return checkPasswd("SELECT phone_passwd FROM contact.phone_info where phone_num = " + phoneNum, passwd);
    }

    public static boolean checkLoginUser(String userName, String passwd) {

        return checkPasswd("select user_passwd from user_info where user_name=" + userName, passwd);
    }

    private static boolean checkPasswd(String sql, String passwd) {
        SqlHelper sh = new SqlHelper();
        ResultSet rs = sh.queryExecute(sql);

        try {
            if (rs.next()) {
            	String dbPasswd = rs.getString(1);
            	System.out.println("passwd = " + dbPasswd);
            	
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
}
