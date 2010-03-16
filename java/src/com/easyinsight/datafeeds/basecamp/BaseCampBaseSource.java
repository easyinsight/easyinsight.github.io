package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.util.Map;

/**
 * User: jamesboe
 * Date: Mar 1, 2010
 * Time: 9:55:12 AM
 */
public abstract class BaseCampBaseSource extends ServerDataSourceDefinition {

    protected static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    protected String retrieveContactInfo(HttpClient client, Builder builder, Map<String, String> peopleCache, String contactId, String url) throws BaseCampLoginException, ParsingException {
        String contactName = null;
        try {
            if(contactId != null) {
                contactName = peopleCache.get(contactId);
                if(contactName == null) {
                    Document contactInfo = runRestRequest("/contacts/person/" + contactId, client, builder, url, null, false);
                    contactName = queryField(contactInfo, "/person/first-name/text()") + " " + queryField(contactInfo, "/person/last-name/text()");
                    peopleCache.put(contactId, contactName);
                }

            }
            return contactName;
        } catch (BaseCampLoginException e) {
            return "";
        }
    }

    protected static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    protected Document runRestRequest(String path, HttpClient client, Builder builder, String url, EIPageInfo pageInfo, boolean badCredentialsOnError) throws BaseCampLoginException, ParsingException {
        HttpMethod restMethod = new GetMethod(url + path);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        Document doc;
        try {
            client.executeMethod(restMethod);
            doc = builder.build(restMethod.getResponseBodyAsStream());
            if(pageInfo != null) {
                pageInfo.MaxPages = Integer.parseInt(restMethod.getResponseHeader("X-Pages").getValue());
            }
        }
        catch (nu.xom.ParsingException e) {
            String statusLine = restMethod.getStatusLine().toString();
            if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                throw new BaseCampLoginException("Could not locate a Basecamp instance at " + url);
            } else {
                if (badCredentialsOnError) {
                    throw new BaseCampLoginException("Invalid Basecamp authentication token in connecting to " + url + "--you can find the token under your the My Info link in the upper right corner on your Basecamp page.");
                } else {
                    throw e;
                }
            }
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return doc;
    }
}
