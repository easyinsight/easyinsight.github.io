package com.easyinsight.datafeeds.zendesk;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.net.URLEncoder;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 5:58 PM
 */
public class ZendeskTest {
    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("jboe@easy-insight.com", "e1ernity");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        //HttpMethod restMethod = new GetMethod("http://easyinsight.zendesk.com/search.xml?query=\"type:ticket updated>2011-02-01\"");
        /*String queryString = URLEncoder.encode("\"type:ticket updated>2011-02-01\"", "UTF-8");
        HttpMethod restMethod = new GetMethod("http://easyinsight.zendesk.com/search.xml?query=" + queryString);*/
        HttpMethod restMethod = new GetMethod("http://easyinsight.zendesk.com/ticket_fields.xml");
        client.executeMethod(restMethod);
        System.out.println(restMethod.getResponseBodyAsString());
    }
}
