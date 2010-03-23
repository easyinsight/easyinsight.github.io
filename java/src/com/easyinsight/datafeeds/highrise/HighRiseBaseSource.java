package com.easyinsight.datafeeds.highrise;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.auth.AuthScope;
import nu.xom.Document;
import nu.xom.Builder;
import nu.xom.Node;
import nu.xom.Nodes;

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

    protected static Document runRestRequest(String path, HttpClient client, Builder builder, String url) throws HighRiseLoginException {
        HttpMethod restMethod = new GetMethod(url + path);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        Document doc;
        try {
            client.executeMethod(restMethod);
            doc = builder.build(restMethod.getResponseBodyAsStream());
        }
        catch (nu.xom.ParsingException e) {
                throw new HighRiseLoginException("Invalid username/password.");
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
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
