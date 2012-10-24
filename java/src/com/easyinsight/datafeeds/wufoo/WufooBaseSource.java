package com.easyinsight.datafeeds.wufoo;

import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

/**
 * User: jamesboe
 * Date: 10/22/12
 * Time: 5:00 PM
 */
public abstract class WufooBaseSource extends ServerDataSourceDefinition {

    protected static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    protected static Document runRestRequest(String path, HttpClient client, WufooCompositeSource parentDefinition) throws ParsingException, ReportException, IOException {
        HttpMethod restMethod = new GetMethod(parentDefinition.getUrl() + path);

        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");

        client.executeMethod(restMethod);

        System.out.println(restMethod.getResponseBodyAsString());

        return new Builder().build(restMethod.getResponseBodyAsStream());
    }
}
