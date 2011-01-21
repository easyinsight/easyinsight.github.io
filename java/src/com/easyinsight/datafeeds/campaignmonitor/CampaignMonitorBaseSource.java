package com.easyinsight.datafeeds.campaignmonitor;

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
import org.apache.http.client.HttpResponseException;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: 12/27/10
 * Time: 12:35 PM
 */
public abstract class CampaignMonitorBaseSource extends ServerDataSourceDefinition {

    protected String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0) {
            String str = results.get(0).getValue();
            try {
                int num = Integer.parseInt(str);
                return String.valueOf(num);
            } catch (NumberFormatException e) {
                return str;
            }
        } else
            return null;
    }

    protected Document query(String queryString, String apiKey, FeedDefinition parentSource) throws IOException, ParsingException {
        try {
            HttpClient client = new HttpClient();
            client.getParams().setAuthenticationPreemptive(true);
            Credentials defaultcreds = new UsernamePasswordCredentials(apiKey, "");
            client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            HttpMethod restMethod = new GetMethod("http://api.createsend.com/api/v3/" + queryString);
            restMethod.setRequestHeader("Accept", "application/xml");
            restMethod.setRequestHeader("Content-Type", "application/xml");
            client.executeMethod(restMethod);
            System.out.println(restMethod.getResponseBodyAsString());
            Document doc = new Builder().build(restMethod.getResponseBodyAsStream());
            return doc;
        } catch (HttpResponseException e) {
            if ("Unauthorized".equals(e.getMessage())) {
                throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Constant Contact data.", parentSource));
            } else {
                throw e;
            }
        }
    }
}
