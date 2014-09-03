package com.jiec.contact.db;

import java.sql.ResultSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ContactHelper {
	 	public static JSONObject getContact(String userId) {
	 		String sql = "select company_info.company_id,company_info.company_name," +
	 				"contact_detail.* from contact_detail,company_info " +
	 				"where contact_detail.contact_own_id = " + userId + " " + 
	 				"and contact_detail.contact_company_id = company_info.company_id";
	 		
	        SqlHelper sh = new SqlHelper();
	        ResultSet rs = sh.queryExecute(sql);
	        
	        JSONObject contactsObject = new JSONObject();

	        try {
	        	String lastCompanyId = "";
	        	JSONObject perContact = null;
	        	JSONArray contactArray = new JSONArray();
	        	JSONArray companyArray = new JSONArray();
	            while (rs.next()) {	            	
	            	lastCompanyId = rs.getString(1);
	            	
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
	                
	                if (!rs.getString(1).equals(lastCompanyId)) {
	            		if (perContact != null) {
	            			JSONObject object = new JSONObject();
	            			object.put("company_id", lastCompanyId);
	            			object.put("company_contacts", companyArray);
	            			contactArray.add(object);
	            		}
	            		companyArray = new JSONArray();
	            	} 
	                
	            }
	            
	            if (companyArray.size() > 0) {
        			JSONObject object = new JSONObject();
        			object.put("company_id", lastCompanyId);
        			object.put("company_contacts", companyArray);
        			contactArray.add(object);
        		}
	            
	            contactsObject.put("data", contactArray);
	            
	            System.out.println("contact = " + contactsObject.toString());
	            
	            rs.close();
                sh.close();

	        } catch (Exception e) {
	            e.printStackTrace();	    
	        }

	        return contactsObject;
	    }
	    
}
