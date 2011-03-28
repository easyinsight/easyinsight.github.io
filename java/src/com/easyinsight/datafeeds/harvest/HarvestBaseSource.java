package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.logging.LogClass;
import nu.xom.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 7:35 PM
 */
public abstract class HarvestBaseSource extends ServerDataSourceDefinition {
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

    protected static Document runRestRequest(String path, HttpClient client, Builder builder, String url, boolean badCredentialsOnError, FeedDefinition parentDefinition, boolean logRequest) throws ParsingException, ReportException {
        System.out.println(url + path);
        HttpMethod restMethod = new GetMethod(url + path);
        /*try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
        }*/
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        boolean successful = false;
        Document doc = null;
        int retryCount = 0;
        do {

            try {
                client.executeMethod(restMethod);
                if (logRequest) {
                    System.out.println(restMethod.getResponseBodyAsString());
                }
                doc = builder.build(restMethod.getResponseBodyAsStream());
                String statusLine = restMethod.getStatusLine().toString();
                System.out.println(statusLine);
                if(statusLine.indexOf("503") != -1 || statusLine.indexOf("502") != -1) {
                    retryCount++;
                    Header retryHeader = restMethod.getResponseHeader("Retry-After");
                    if (retryHeader == null) {
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e1) {
                        }
                    } else {
                        int retryTime = Integer.parseInt(retryHeader.getValue());
                        int time = retryTime * 1000;
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e1) {
                        }
                    }
                } else {
                    String rootValue = doc.getRootElement().getValue();
                    successful = true;
                }
            } catch (IOException e) {
                System.out.println("IOException " + e.getMessage());
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
                System.out.println(e.getMessage());
                retryCount++;
                String statusLine = restMethod.getStatusLine().toString();
                if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Harvest instance at " + url, parentDefinition));
                } else if (statusLine.indexOf("503") != -1) {
                    Header retryHeader = restMethod.getResponseHeader("Retry-After");
                    if (retryHeader == null) {
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e1) {
                        }
                    } else {
                        int retryTime = Integer.parseInt(retryHeader.getValue());
                        int time = retryTime * 1000;
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e1) {
                        }
                    }
                } else if (statusLine.indexOf("403") != -1) {
                    throw new RuntimeException("403 error");
                } else {
                    if (badCredentialsOnError) {
                        throw new ReportException(new DataSourceConnectivityReportFault("Invalid Harvest credentials in connecting to " + url + ".", parentDefinition));
                    } else {
                        LogClass.error("Unrelated parse error with status line " + statusLine);
                        throw e;
                    }
                }
            } catch (ReportException re) {
                throw re;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Harvest could not be reached due to a large number of current users, please try again in a bit.");
        }
        return doc;
    }
}
