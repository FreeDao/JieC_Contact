
package com.jiec.contact.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlHelper {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        SqlHelper ssh = new SqlHelper();
        ResultSet rs = ssh.queryExecute("select * from dbo.Lxr_Detail");
        try {
            while (rs.next()) {
                System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private PreparedStatement mPreparedStatement = null;

    private Connection mConnection = null;

    private ResultSet mResultSet = null;

    private static final String DRIVER_STR = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String SQL_URL = "jdbc:sqlserver://120.24.58.159:1433;databaseName=contact";

    public SqlHelper() {

        try {
            Class.forName(DRIVER_STR);
            mConnection = (Connection) DriverManager.getConnection(SQL_URL, "sa", "123456");

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
            mResultSet = st.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mResultSet;
    }

    public boolean upExecute(String sql) {
        try {
            mPreparedStatement = mConnection.prepareStatement(sql);

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
