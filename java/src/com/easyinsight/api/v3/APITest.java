package com.easyinsight.api.v3;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:44 PM
 */
public class APITest {
    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("jboe", "e1ernity");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        {
            PostMethod postMethod = new PostMethod("http://localhost:8080/app/defineDataSource");
            String content = "<defineDataSource><dataSourceName>HTTP Data Source</dataSourceName><fields><field dataType=\"grouping\"><key>Blah</key></field><field dataType=\"measure\"><key>Count</key></field></fields></defineDataSource>";
            System.out.println(content);
            StringRequestEntity entity = new StringRequestEntity(content, "text/xml", "UTF-8");
            postMethod.setRequestEntity(entity);
            client.executeMethod(postMethod);
            System.out.println(postMethod.getResponseBodyAsString());
        }
        {
            PostMethod postMethod = new PostMethod("http://localhost:8080/app/replaceRows");
            String content = "<rows dataSourceName=\"HTTP Data Source\"><row><Blah>Acme</Blah><Count>1</Count></row></rows>";
            System.out.println(content);
            StringRequestEntity entity = new StringRequestEntity(content, "text/xml", "UTF-8");
            postMethod.setRequestEntity(entity);
            client.executeMethod(postMethod);
            System.out.println(postMethod.getResponseBodyAsString());
        }
    }
}
