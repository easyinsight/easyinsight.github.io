package com.easyinsight.salesautomation;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/31/13
 * Time: 3:47 PM
 */
public class PersonalizedSalesEmail {
    public void sync() {

        EIConnection conn = Database.instance().getConnection();
        try {
            HttpClient client = new HttpClient();
            client.getParams().setAuthenticationPreemptive(true);

            Credentials defaultcreds = new UsernamePasswordCredentials("2d60bbb7-8f96-4744-8824-558f11155d73", "x");
            client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);

            GetMethod userMethod = new GetMethod("https://api.insight.ly/v1/users");
            client.executeMethod(userMethod);
            JSONArray users = (JSONArray) JSONValue.parse(new BufferedReader(new InputStreamReader(userMethod.getResponseBodyAsStream())));
            Map<String, String> userMap = new HashMap<String, String>();
            for (Object userObject : users) {
                JSONObject user = (JSONObject) userObject;
                userMap.put(user.get("USER_ID").toString(), user.get("FIRST_NAME").toString() + " " + user.get("LAST_NAME").toString());
            }

            Map<String, JSONObject> opportunityMap = new HashMap<String, JSONObject>();
            GetMethod opportunitiesMethod = new GetMethod("https://api.insight.ly/v1/opportunities");
            client.executeMethod(opportunitiesMethod);
            JSONArray existingOpportunities = (JSONArray) JSONValue.parse(new BufferedReader(new InputStreamReader(opportunitiesMethod.getResponseBodyAsStream())));
            for (Object existingOpportunity : existingOpportunities) {
                JSONObject contactObject = (JSONObject) existingOpportunity;
                String accountID = (String) contactObject.get("OPPORTUNITY_FIELD_1");
                opportunityMap.put(accountID, contactObject);
            }

            PreparedStatement stmt = conn.prepareStatement("SELECT ACCOUNT.ACCOUNT_ID, USER.USER_ID, USER.FIRST_NAME, USER.NAME, USER.EMAIL, WELCOME_TYPE, PERSONALIZED_SALES_EMAIL_ID " +
                    "FROM PERSONALIZED_SALES_EMAIL, USER, ACCOUNT WHERE " +
                    "PERSONALIZED_SALES_EMAIL.USER_ID = USER.USER_ID AND USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND ACCOUNT.ACCOUNT_STATE = ?");
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM PERSONALIZED_SALES_EMAIL WHERE PERSONALIZED_SALES_EMAIL_ID = ?");
            stmt.setInt(1, Account.TRIAL);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long accountID = rs.getLong(1);
                long userID = rs.getLong(2);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                String email = rs.getString(5);
                int type = rs.getInt(6);
                long id = rs.getLong(7);
                String repEmail;
                String repPhone;
                JSONObject insightlyOpp = opportunityMap.get(String.valueOf(accountID));
                if (insightlyOpp == null) {
                    // no opportunity
                } else {
                    Object statusField = insightlyOpp.get("OPPORTUNITY_FIELD_2");
                    boolean send = (statusField != null && "Send".equals(statusField.toString()));
                    Object responsiblePartyObject = insightlyOpp.get("RESPONSIBLE_PARTY");
                    String responsibleUser = null;
                    if (responsiblePartyObject != null) {
                        responsibleUser = userMap.get(responsiblePartyObject.toString());
                    }
                    if (!send) {
                        clearStmt.setLong(1, id);
                        clearStmt.executeUpdate();
                    }
                    if (send && responsibleUser != null) {
                        if ("James Boe".equals(responsibleUser)) {
                            repEmail = "jboe@easy-insight.com";
                            repPhone = "720-220-8085";
                        } else if ("Cendie Lee".equals(responsibleUser)) {
                            repEmail = "cendie@easy-insight.com";
                            repPhone = "720-220-8085";
                        } else if ("Alan Baldwin".equals(responsibleUser)) {
                            repEmail = "abaldwin@easy-insight.com";
                            repPhone = "720-220-8085";
                        } else {
                            throw new RuntimeException();
                        }
                        clearStmt.setLong(1, id);
                        clearStmt.executeUpdate();
                        String name;
                        if (firstName == null || "".equals(firstName)) {
                            name = lastName;
                        } else {
                            name = firstName;
                        }
                        SalesEmail.personalizedNurture(responsibleUser, name, email, repEmail, repPhone);
                    }
                }
            }
            stmt.close();
            clearStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
