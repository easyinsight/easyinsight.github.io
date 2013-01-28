package com.easyinsight.crm;

import com.easyinsight.database.Database;
import com.easyinsight.users.Account;
import com.easyinsight.users.User;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/23/13
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class InsightlyCRMSync {

    public static void main(String[] args) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);

        Credentials defaultcreds = new UsernamePasswordCredentials("2d60bbb7-8f96-4744-8824-558f11155d73", "x");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        GetMethod gm = new GetMethod("https://api.insight.ly/v1/organisations");
        try {
            client.executeMethod(gm);
            System.out.println(gm.getResponseBodyAsString());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        int x = 0;
        if(x == 0) return;
        Database.initialize();
        Session s = Database.instance().createSession();
        List<Account> accounts = (List<Account>) s.createQuery("from Account").list();
        for (Account a : accounts) {
            try {
                PostMethod post = new PostMethod("https://api.insight.ly/v1/organisations");

                JSONObject jo = new JSONObject();
                jo.put("ORGANISATION_NAME", a.getName());
                jo.put("ORGANISATION_FIELD_1", String.valueOf(a.getAccountID()));
                jo.put("VISIBLE_TO", "EVERYONE");
                post.setRequestHeader("Accept", "application/json");

                post.setRequestEntity(new StringRequestEntity(jo.toString(), "application/json", "UTF-8"));
                client.executeMethod(post) ;
                JSONObject jj = (JSONObject) JSONValue.parse(new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream())));
                Long orgID = (Long) jj.get("ORGANISATION_ID");
                System.out.println(orgID);
                for(User u : a.getUsers()) {
                    if(u.isAccountAdmin()) {
                        JSONObject contact = new JSONObject();
                        contact.put("VISIBLE_TO","EVERYONE");
                        contact.put("FIRST_NAME", u.getFirstName());
                        contact.put("LAST_NAME", u.getName());
                        contact.put("CONTACT_FIELD_1", String.valueOf(u.getUserID()));
                        JSONObject contactInfo = new JSONObject();
                        contactInfo.put("TYPE", "EMAIL");
                        contactInfo.put("LABEL", "Current");
                        contactInfo.put("DETAIL", u.getEmail());
                        JSONArray ja = new JSONArray();
                        ja.add(contactInfo);
                        contact.put("CONTACT_INFO", ja);
                        contact.put("DEFAULT_LINKED_ORGANISATION", String.valueOf(orgID));
                        PostMethod contactMethod = new PostMethod("https://api.insight.ly/v1/contacts");
                        contactMethod.setRequestHeader("Accept", "application/json");
                        System.out.println(contact.toString());
                        contactMethod.setRequestEntity(new StringRequestEntity(contact.toString(), "application/json", "UTF-8"));
                        client.executeMethod(contactMethod);
                        System.out.println(contactMethod.getResponseBodyAsString());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
}
