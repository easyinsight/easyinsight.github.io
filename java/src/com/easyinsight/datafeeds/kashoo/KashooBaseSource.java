package com.easyinsight.datafeeds.kashoo;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * User: jamesboe
 * Date: 10/28/11
 * Time: 10:42 PM
 */
public abstract class KashooBaseSource extends ServerDataSourceDefinition {
    protected static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    protected static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    protected static Document runRestRequest(String path, HttpClient client, Builder builder, String url, FeedDefinition parentDefinition) throws ParsingException, ReportException {
        HttpMethod restMethod = new GetMethod(url + path);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        Document doc;

        try {
            client.executeMethod(restMethod);
            doc = builder.build(restMethod.getResponseBodyAsStream());
            return doc;
        } catch (ParsingException pe) {
            String statusLine = restMethod.getStatusLine().toString();
            if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Kashoo instance at " + url, parentDefinition));
            } else if (statusLine.contains("401")) {
                throw new ReportException(new DataSourceConnectivityReportFault("Your API key was invalid.", parentDefinition));
            } else {
                throw new RuntimeException(pe);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
