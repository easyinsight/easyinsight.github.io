package com.easyinsight.datafeeds.kashoo;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
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
    public static int PAGE_LIMIT = 100;

    protected static HttpClient getHttpClient() {
        HttpClient client = new HttpClient();
        return client;
    }

    protected static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    protected static Object runRestRequest(String path, HttpClient client, FeedDefinition parentDefinition, String token) throws ParsingException, ReportException {
        HttpMethod restMethod = new GetMethod("https://api.kashoo.com" + path);
        restMethod.setRequestHeader("Accept", "application/json");
        restMethod.setRequestHeader("Content-Type", "application/json");
        restMethod.setRequestHeader("Authorization", "TOKEN json:" + token);
        try {
            client.executeMethod(restMethod);
            JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            Object postObject = parser.parse(restMethod.getResponseBodyAsStream());
            return postObject;
        } catch (Exception pe) {
            if (restMethod.getStatusLine() == null) {
                throw new ReportException(new DataSourceConnectivityReportFault("Your API key was invalid.", parentDefinition));
            }
            String statusLine = restMethod.getStatusLine().toString();
            if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Kashoo instance.", parentDefinition));
            } else if (statusLine.contains("401")) {
                throw new ReportException(new DataSourceConnectivityReportFault("Your API key was invalid.", parentDefinition));
            } else {
                throw new RuntimeException(pe);
            }
        }
    }
}
