package com.easyinsight.datafeeds.highrise;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.basecamp.BaseCampDataException;
import nu.xom.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.auth.AuthScope;

import java.io.IOException;
import java.util.Map;

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

    protected String retrieveContactInfo(HttpClient client, Builder builder, Map<String, String> peopleCache, String contactId, String url) throws HighRiseLoginException, ParsingException {
        try {
            String contactName = null;
            if(contactId != null) {
                contactName = peopleCache.get(contactId);
                if(contactName == null) {
                    Document contactInfo = runRestRequest("/people/person/" + contactId, client, builder, url, false, false);
                    contactName = queryField(contactInfo, "/person/first-name/text()") + " " + queryField(contactInfo, "/person/last-name/text()");
                    peopleCache.put(contactId, contactName);
                }

            }
            return contactName;
        } catch (HighRiseLoginException e) {
            peopleCache.put(contactId, "");
            return "";
        }
    }

    protected String retrieveUserInfo(HttpClient client, Builder builder, Map<String, String> peopleCache, String contactId, String url) throws HighRiseLoginException, ParsingException {
        try {
            String contactName = null;
            if(contactId != null) {
                contactName = peopleCache.get(contactId);
                if(contactName == null) {
                    Document contactInfo = runRestRequest("/users/" + contactId, client, builder, url, false, false);
                    contactName = queryField(contactInfo, "/user/name/text()");
                    peopleCache.put(contactId, contactName);
                }

            }
            return contactName;
        } catch (HighRiseLoginException e) {
            peopleCache.put(contactId, "");
            return "";
        }
    }

    protected static Document runRestRequest(String path, HttpClient client, Builder builder, String url, boolean badCredentialsOnError, boolean logRequest) throws HighRiseLoginException, ParsingException {
        HttpMethod restMethod = new GetMethod(url + path);
        try {
            Thread.sleep(150);
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
                if (logRequest) {
                    System.out.println(restMethod.getResponseBodyAsString());
                }
                doc = builder.build(restMethod.getResponseBodyAsStream());                
                String rootValue = doc.getRootElement().getValue();
                if ("The API is not available to this account".equals(rootValue)) {
                    throw new BaseCampDataException("You need to enable API access to your Highrise account--you can do this under Account (Upgrade/Invoice), Highrise API in the Highrise user interface.");
                }
                successful = true;
            } catch (IOException e) {
                retryCount++;
                if (e.getMessage().contains("503")) {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e1) {
                    }
                } else {
                    throw new RuntimeException(e);
                }
            } catch (nu.xom.ParsingException e) {
                e.printStackTrace();
                retryCount++;
                String statusLine = restMethod.getStatusLine().toString();
                if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                    throw new HighRiseLoginException("Could not locate a Highrise instance at " + url);
                } else if ("HTTP/1.1 503 Service Temporarily Unavailable".equals(statusLine)) {
                    System.out.println("Highrise 503, retrying");
                    Header retryHeader = restMethod.getResponseHeader("Retry-After");
                    if (retryHeader == null) {
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e1) {
                        }
                    } else {
                        int time = Integer.parseInt(retryHeader.getValue()) * 1000;
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e1) {
                        }
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
