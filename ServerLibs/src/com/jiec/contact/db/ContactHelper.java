
package com.jiec.contact.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jiec.contact.utils.LogUtil;

public class ContactHelper {
    public static JSONObject getContact(String userId) {
        String sql = "select Lxr.BH,Lxr.ZZ_MC," + "Lxr_Detail.* from Lxr_Detail,Lxr "
                + "where Lxr_Detail.CC = " + userId + " "
                + "and Lxr_Detail.BH = Lxr.BH ORDER BY Lxr.BH";

        SqlHelper sh = new SqlHelper();
        ResultSet rs = sh.queryExecute(sql);

        JSONObject contactsObject = new JSONObject();

        try {
            String lastCompanyId = "";
            String lastCompanyName = "";
            JSONObject perContact = null;
            JSONArray contactArray = new JSONArray();
            JSONArray companyArray = new JSONArray();

            while (rs.next()) {

                if (!rs.getString(1).equals(lastCompanyId)) {
                    if (perContact != null) {
                        addCompanyToContacts(lastCompanyId, lastCompanyName, contactArray,
                                companyArray);
                    }
                    companyArray = new JSONArray();
                }

                lastCompanyId = rs.getString(1);
                lastCompanyName = rs.getString(2);

                perContact = new JSONObject();
                perContact.put("contact_id", rs.getString(19));
                perContact.put("contact_name", rs.getString(4));
                perContact.put("contact_bgdh_1", rs.getString(5));
                perContact.put("contact_bgdh_2", rs.getString(6));
                perContact.put("contact_bgdh_3", rs.getString(7));
                perContact.put("contact_yddh_1", rs.getString(8));
                perContact.put("contact_yddh_2", rs.getString(9));
                perContact.put("contact_yddh_3", rs.getString(10));
                perContact.put("contact_company_id", rs.getString(3));
                perContact.put("contact_qq", rs.getString(12));
                perContact.put("contact_msn", rs.getString(13));
                perContact.put("contact_email_1", rs.getString(14));
                perContact.put("contact_email_2", rs.getString(15));
                perContact.put("contact_email_3", rs.getString(16));
                perContact.put("contact_edit_user_id", rs.getString(11));
                perContact.put("contact_last_edit_time", rs.getString(17));
                perContact.put("contact_type", rs.getInt(20));

                companyArray.add(perContact);

            }

            if (companyArray.size() > 0) {
                addCompanyToContacts(lastCompanyId, lastCompanyName, contactArray, companyArray);
            }

            contactsObject.put("data", contactArray);

            LogUtil.d("contact = " + contactsObject.toString());

            rs.close();
            sh.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return contactsObject;
    }

    private static void addCompanyToContacts(String id, String name, JSONArray contactArray,
            JSONArray array) {
        JSONObject object = new JSONObject();
        object.put("company_id", id);
        object.put("company_name", name);
        object.put("company_contacts", array);
        contactArray.add(object);
    }

    public static void insertContact(JSONObject object, JSONObject replayObject) {

        String sql = "insert into Lxr_Detail (XM, BGDH1, BGDH2, BGDH3,"
                + "YDDH1, YDDH2, YDDH3, BH, QQHM, " + "DZYS1, DZYS2, DZYS3, SJLLR, XGSJ, Type) "
                + "values('"
                + object.getString("contact_name")
                + "', '"
                + object.getString("contact_bgdh_1")
                + "', '"
                + object.getString("contact_bgdh_2")
                + "', '"
                + object.getString("contact_bgdh_3")
                + "', '"
                + object.getString("contact_yddh_1")
                + "', '"
                + object.getString("contact_yddh_2")
                + "', '"
                + object.getString("contact_yddh_3")
                + "', '"
                + object.getString("contact_company_id")
                + "', '"
                + object.getString("contact_qq")
                + "', '"
                + object.getString("contact_email_1")
                + "', '"
                + object.getString("contact_email_2")
                + "', '"
                + object.getString("contact_email_3")
                + "', '"
                + object.getString("contact_own_id")
                + "', '"
                + object.getString("contact_last_edit_time")
                + "', "
                + object.getInt("contact_type") + ");";
        SqlHelper sh = new SqlHelper();

        if (sh.upExecute(sql)) {
            replayObject.put("result", 1);

            updateCompanyInfo(object.getString("contact_company_id"),
                    object.getString("contact_name"));
        } else {
            replayObject.put("result", -1);
            return;
        }

        sql = "select ID from Lxr_Detail where BH = '" + object.getString("contact_company_id")
                + "' and XM = '" + object.getString("contact_name") + "';";

        ResultSet rs = sh.queryExecute(sql);
        try {
            if (rs.next()) {
                replayObject.put("contact_id", rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新公司名字字段
     * 
     * @param name
     */
    public static void updateCompanyInfo(final String companyId, final String name) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String sql = "select XM1, XM2, XM3, XM4, XM5, XM6, XM7, XM8 from Lxr where BH = '"
                        + companyId + "';";
                SqlHelper sh = new SqlHelper();
                ResultSet rs = sh.queryExecute(sql);

                int index = 1;
                try {
                    if (rs.next()) {
                        for (; index < 9; index++) {
                            if (rs.getString(index) == null) {
                                break;
                            }

                        }

                    }

                    if (index == 9)
                        return;

                    sql = "update Lxr SET XM" + index + " = '" + name + "' where BH = '"
                            + companyId + "';";

                    if (sh.upExecute(sql)) {
                        LogUtil.d("更新到Lxr成功");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void udpateContact(JSONObject object, JSONObject replayObject) {
        String sql = "UPDATE Lxr_Detail SET XM = '" + object.getString("contact_name")
                + "', BGDH1 = '" + object.getString("contact_bgdh_1") + "', BGDH2 = '"
                + object.getString("contact_bgdh_2") + "', BGDH3 = '"
                + object.getString("contact_bgdh_3") + "', YDDH1 = '"
                + object.getString("contact_yddh_1") + "', YDDH2 = '"
                + object.getString("contact_yddh_2") + "', YDDH3 = '"
                + object.getString("contact_yddh_3") + "', BH = '"
                + object.getString("contact_company_id") + "', QQHM = '"
                + object.getString("contact_qq") + "', DZYS1 = '"
                + object.getString("contact_email_1") + "', DZYS2 = '"
                + object.getString("contact_email_2") + "', DZYS3 = '"
                + object.getString("contact_email_3") + "', CallPerson = '"
                + object.getString("contact_last_edit_time") + "' WHERE ID = "
                + object.getInt("contact_id") + ";";
        System.out.println(sql);
        SqlHelper sh = new SqlHelper();

        if (sh.upExecute(sql)) {
            replayObject.put("result", 1);
            replayObject.put("contact", object);
        } else {
            replayObject.put("result", -1);
            return;
        }
    }
}
