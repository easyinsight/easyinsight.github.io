package com.easyinsight.datafeeds.basecamp;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 24, 2009
 * Time: 11:35:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class BaseCampBugReportProvider {

    private static final String BASECAMP_API_USERNAME = "78cb157999474d892228c3a35b5b99a268a5bd50";
    private static final String BASECAMP_API_PASSWORD = "";
    private static final String BASECAMP_API_ENDPOINT = "https://easyinsight.basecamphq.com/todo_lists";
    private static final String GENERAL_BUGS = "Generic";
    private static final String GENERAL_BUGS_TODO = "/6600933/todo_items.xml";


    private static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    public void reportBug(String category, String text, String username) {
        try {
            HttpClient client = getHttpClient(BASECAMP_API_USERNAME, BASECAMP_API_PASSWORD);
            String todoList = null;
            if(GENERAL_BUGS.equals(category)) {
                todoList = GENERAL_BUGS_TODO;
            }
            PostMethod method = new PostMethod(BASECAMP_API_ENDPOINT + todoList);
            String content = "<todo-item><content>" + username + ": " + StringEscapeUtils.escapeXml(text) + "</content></todo-item>"; 
            StringRequestEntity entity = new StringRequestEntity(content, "text/xml", "UTF-8");
            method.setRequestEntity(entity);
            client.executeMethod(method);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        }
    }
}
