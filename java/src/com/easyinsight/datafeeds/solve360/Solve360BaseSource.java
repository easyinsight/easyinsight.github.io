package com.easyinsight.datafeeds.solve360;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import nu.xom.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * User: jamesboe
 * Date: 10/28/11
 * Time: 10:42 PM
 */
public abstract class Solve360BaseSource extends ServerDataSourceDefinition {
    protected static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    protected static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if (results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    protected static Document runRestRequest(String path, HttpClient client, Builder builder, FeedDefinition parentDefinition) throws ParsingException, ReportException {
        GetMethod restMethod = new GetMethod(path);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        Document doc;

        try {

            client.executeMethod(restMethod);
            doc = builder.build(restMethod.getResponseBodyAsStream());
            return doc;
        } catch (ParsingException pe) {
            String statusLine = restMethod.getStatusLine().toString();
            if (statusLine.contains("401")) {
                throw new ReportException(new DataSourceConnectivityReportFault("Your API key was invalid.", parentDefinition));
            } else {
                throw new RuntimeException(pe);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static Document runUglyRestRequest(String path, Builder builder, FeedDefinition parentDefinition) throws ParsingException, ReportException {
        Solve360CompositeSource solve360CompositeSource = (Solve360CompositeSource) parentDefinition;
        try {
            URL url = new URL(path);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setRequestMethod("GET");
            String userpassword = solve360CompositeSource.getUserEmail() + ":" + solve360CompositeSource.getAuthKey();
            String encodedAuthorization = new String(Base64.encodeBase64(userpassword.getBytes()));
            connection.setRequestProperty("Authorization", "Basic " +
                    encodedAuthorization);
            connection.connect();
            String query = "<request><layout>1</layout></request>";
            DataOutputStream output = new DataOutputStream(connection.getOutputStream());

            output.writeBytes(query);
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Document doc = builder.build(rd);

            connection.disconnect();
            return doc;
        } catch (IOException ie) {
            throw new ReportException(new DataSourceConnectivityReportFault(ie.getMessage(), parentDefinition));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
