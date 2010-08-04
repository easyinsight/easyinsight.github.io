package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.logging.LogClass;
import nu.xom.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
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
            peopleCache.put(contactId, "");
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

    protected static Document runRestRequest(String path, HttpClient client, Builder builder, String url, EIPageInfo pageInfo, boolean badCredentialsOnError) throws BaseCampLoginException, ParsingException {
        HttpMethod restMethod = new GetMethod(url + path);
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
        }
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        boolean successful = false;
        Document doc = null;
        int retryCount = 0;
        do {

            try {
                client.executeMethod(restMethod);
                //System.out.println(restMethod.getResponseBodyAsString());
                doc = builder.build(restMethod.getResponseBodyAsStream());
                String rootValue = doc.getRootElement().getValue();
                if ("The API is not available to this account".equals(rootValue)) {
                    throw new BaseCampDataException("You need to enable API access to your Basecamp account--you can do this under Account (Upgrade/Invoice), Basecamp API in the Basecamp user interface.");
                }
                //Thread.dumpStack();
                //System.out.println(doc.getRootElement().getValue());
                if(pageInfo != null) {
                    pageInfo.MaxPages = Integer.parseInt(restMethod.getResponseHeader("X-Pages").getValue());
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
            } catch (ParsingException e) {
                retryCount++;
                String statusLine = restMethod.getStatusLine().toString();
                if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                    throw new BaseCampLoginException("Could not locate a Basecamp instance at " + url);
                } else if (statusLine.indexOf("503") != -1 ||
                        statusLine.indexOf("403") != -1) {
                    System.out.println(statusLine);
                    Header retryHeader = restMethod.getResponseHeader("Retry-After");
                    if (retryHeader == null) {
                        System.out.println("no retry header");
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e1) {
                        }
                    } else {
                        int retryTime = Integer.parseInt(retryHeader.getValue());
                        System.out.println("retry time = " + retryTime);
                        if (retryTime < 20) {
                            retryTime = 20;
                        }
                        int time = retryTime * 1000;
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e1) {                            
                        }
                    }
                } else {
                    if (badCredentialsOnError) {
                        throw new BaseCampLoginException("Invalid Basecamp authentication token in connecting to " + url + "--you can find the token under your the My Info link in the upper right corner on your Basecamp page.");
                    } else {
                        LogClass.error("Unrelated parse error with status line " + statusLine);
                        throw e;
                    }
                }
            } catch (BaseCampLoginException ble) {
                throw ble;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Basecamp could not be reached due to a large number of current users, please try again in a bit.");
        }
        return doc;
    }
}
