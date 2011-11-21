package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.logging.LogClass;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 6:38 PM
 */
public abstract class ZendeskBaseSource extends ServerDataSourceDefinition {

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

    public Document runRestRequest(ZendeskCompositeSource zendeskCompositeSource, HttpClient client, String path, Builder builder) throws InterruptedException {
        HttpMethod restMethod = new GetMethod(zendeskCompositeSource.getUrl() + path);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        boolean successful = false;
        Document doc = null;
        int retryCount = 0;
        do {
            try {
                client.executeMethod(restMethod);
                doc = builder.build(restMethod.getResponseBodyAsStream());
                successful = true;
            } catch (Exception e) {
                String statusLine = restMethod.getStatusLine().toString();
                if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Zendesk instance at " +
                            zendeskCompositeSource.getUrl(), zendeskCompositeSource));
                } else if (statusLine.indexOf("503") != -1) {
                    Thread.sleep(20000);
                    retryCount++;
                } else {
                    throw new ReportException(new DataSourceConnectivityReportFault("Invalid Zendesk credentials in connecting to " +
                            zendeskCompositeSource.getUrl() + ".",
                            zendeskCompositeSource));
                }
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Zendesk could not be reached due to a large number of current users, please try again in a bit.");
        }
        return doc;
    }
}
