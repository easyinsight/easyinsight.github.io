package com.easyinsight.datafeeds.highrise;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.basecamp.BaseCampDataException;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.auth.AuthScope;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 11:50:29 PM
 */
public abstract class HighRiseBaseSource extends ServerDataSourceDefinition {

    public static final String XMLDATEFORMAT = "yyyy-MM-dd";
    public static final String XMLDATETIMEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    protected static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    protected static Document runRestRequest(String path, HttpClient client, Builder builder, String url, boolean badCredentialsOnError) throws HighRiseLoginException, ParsingException {
        HttpMethod restMethod = new GetMethod(url + path);
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
        }
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        boolean successful = false;
        int retryCount = 0;
        Document doc = null;
        do {
            try {
                client.executeMethod(restMethod);
                doc = builder.build(restMethod.getResponseBodyAsStream());
                String rootValue = doc.getRootElement().getValue();
                if ("The API is not available to this account".equals(rootValue)) {
                    throw new BaseCampDataException("You need to enable API access to your Highrise account--you can do this under Account (Upgrade/Invoice), Highrise API in the Highrise user interface.");
                }
                successful = true;
            } catch (nu.xom.ParsingException e) {
                retryCount++;
                String statusLine = restMethod.getStatusLine().toString();
                if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                    throw new HighRiseLoginException("Could not locate a Highrise instance at " + url);
                } else if ("HTTP/1.1 503 Service Temporarily Unavailable".equals(statusLine)) {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e1) {
                    }
                } else {
                    if (badCredentialsOnError) {
                        throw new HighRiseLoginException("Invalid Highrise authentication token in connecting to " + url + "--you can find the token under your the My Info link in the upper right corner on your Highrise page.");
                    } else {
                        throw e;
                    }
                }
            }
            catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } while (!successful && retryCount < 3);
        if (!successful) {
            throw new RuntimeException("Highrise could not be reached due to a large number of current users, please try again in a bit.");
        }
        return doc;
    }

    protected static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }
}
