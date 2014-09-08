
package com.jiec.contact.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jiec.contact.utils.LogUtil;

public class ContactHelper {
    public static JSONObject getContact(String userId) {
        String sql = "select company_info.company_id,company_info.company_name,"
                + "contact_detail.* from contact_detail,company_info "
                + "where contact_detail.contact_own_id = " + userId + " "
                + "and contact_detail.contact_company_id = company_info.company_id";

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
                perContact.put("contact_id", rs.getString(3));
                perContact.put("contact_name", rs.getString(4));
                perContact.put("contact_bgdh_1", rs.getString(5));
                perContact.put("contact_bgdh_2", rs.getString(6));
                perContact.put("contact_bgdh_3", rs.getString(7));
                perContact.put("contact_yddh_1", rs.getString(8));
                perContact.put("contact_yddh_2", rs.getString(9));
                perContact.put("contact_yddh_3", rs.getString(10));
                perContact.put("contact_company_id", rs.getString(11));
                perContact.put("contact_qq", rs.getString(12));
                perContact.put("contact_msn", rs.getString(13));
                perContact.put("contact_email_1", rs.getString(14));
                perContact.put("contact_email_2", rs.getString(15));
                perContact.put("contact_email_3", rs.getString(16));
                perContact.put("contact_edit_user_id", rs.getString(18));
                perContact.put("contact_last_edit_time", rs.getString(19));

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
        /**
         * contact_id int auto_increment primary key, contact_name varchar(20)
         * not null, contact_bgdh_1 varchar(20), contact_bgdh_2 varchar(20),
         * contact_bgdh_3 varchar(20), contact_yddh_1 varchar(20),
         * contact_yddh_2 varchar(20), contact_yddh_3 varchar(20),
         * contact_company_id varchar(8), contact_qq varchar(18), contact_msn
         * varchar(18), contact_email_1 varchar(28), contact_email_2
         * varchar(28), contact_email_3 varchar(28), contact_own_id varchar(20),
         * contact_edit_user_id varchar(20), contact_last_edit_time time
         */

        String sql = "insert into contact_detail (contact_name, contact_bgdh_1, contact_bgdh_2, contact_bgdh_3,"
                + "contact_yddh_1, contact_yddh_2, contact_yddh_3, contact_company_id, contact_qq, "
                + "contact_email_1, contact_email_2, contact_email_3, contact_own_id, contact_last_edit_time) "
                + "value('"
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
                + "', '" + object.getString("contact_last_edit_time") + "');";
        SqlHelper sh = new SqlHelper();

        if (sh.upExecute(sql)) {
            replayObject.put("result", 1);
        } else {
            replayObject.put("result", -1);
            return;
        }

        sql = "select contact_id from contact_detail where contact_company_id = '"
                + object.getString("contact_company_id") + "' and contact_name = '"
                + object.getString("contact_name") + "';";

        ResultSet rs = sh.queryExecute(sql);
        try {
            if (rs.next()) {
                replayObject.put("contact_id", rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void udpateContact(JSONObject object, JSONObject replayObject) {
        UPDATE `contact`.`contact_detail` SET `contact_bgdh_1`='10086' WHERE `contact_id`='9';
        String sql = "update contact_detail set contact_name = '" 
                + object.getString("contact_name")
                + "', contact_bgdh_1 = '"
                + object.getString("contact_bgdh_1")
                + "', contact_bgdh_1 = '"
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
                + "', '" + object.getString("contact_last_edit_time") + "');";

                , , contact_bgdh_2, contact_bgdh_3,"
                + "contact_yddh_1, contact_yddh_2, contact_yddh_3, contact_company_id, contact_qq, "
                + "contact_email_1, contact_email_2, contact_email_3, contact_own_id, contact_last_edit_time) "
                + "value('"
               
                + "', '"
                
                        SqlHelper sh = new SqlHelper();

        if (sh.upExecute(sql)) {
            replayObject.put("result", 1);
        } else {
            replayObject.put("result", -1);
            return;
        }
    }
}
