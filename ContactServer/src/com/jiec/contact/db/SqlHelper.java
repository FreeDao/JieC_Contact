
package com.jiec.contact.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlHelper {

    final static int SELECT = 0;

    final static int CREATE = 1;

    final static int DELETE = 2;

    final static int UPDATE = 3;

    final static int STATISTICS = 4;

    private PreparedStatement mPreparedStatement = null;

    private Connection mConnection = null;

    private ResultSet mResultSet = null;

    private static final String DRIVER_STR = "com.mysql.jdbc.Driver";

    private static final String SQL_URL = "jdbc:mysql://114.215.153.4:3306/contact";

    public SqlHelper() {

        try {
            Class.forName(DRIVER_STR);
            mConnection = (Connection) DriverManager.getConnection(SQL_URL, "root", "woshiwbjso");
            System.out.println("Success connect Mysql server!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        try {
            if (mPreparedStatement != null) {
                mPreparedStatement.close();
            }
            if (mConnection != null) {
                mConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet queryExecute(String sql) {

        try {

            Statement st = mConnection.createStatement();
            System.out.println("sql = " + sql);
            mResultSet = st.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mResultSet;
    }

    public boolean upExecute(String sql, String[] parse) {
        try {
            mPreparedStatement = mConnection.prepareStatement(sql);

            for (int i = 0; i < parse.length; i++) {
                mPreparedStatement.setString(i + 1, parse[i]);
            }

            if (mPreparedStatement.executeUpdate() == 1) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
