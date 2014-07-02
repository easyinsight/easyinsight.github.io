package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.logging.LogClass;
import net.minidev.json.parser.JSONParser;
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
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 6:38 PM
 */
public abstract class ZendeskBaseSource extends ServerDataSourceDefinition {

    public static HttpClient getHttpClient(ZendeskCompositeSource source) {
        HttpClient client = new HttpClient();
        client.setTimeout(30000);
        client.getParams().setAuthenticationPreemptive(true);
        String username = source.getZdUserName() + ((source.getZdApiKey() == null || "".equals(source.getZdApiKey().trim())) ? "" : "/token");
        String password = (source.getZdApiKey() == null || "".equals(source.getZdApiKey().trim())) ? source.getZdPassword() : source.getZdApiKey();
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

    protected static String queryField(Map n, String xpath) {
        Object obj = n.get(xpath);
        if(obj != null)
            return obj.toString();
        else
            return null;
    }

    protected Map queryList(String queryString, ZendeskCompositeSource zendeskCompositeSource, HttpClient client) {
        HttpMethod restMethod = new GetMethod(queryString);
        restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
        boolean successful = false;
        int retryCount = 0;
        Map results = null;
        do {
            try {
                //System.out.println(queryString);
                client.executeMethod(restMethod);
                //System.out.println("\t" + restMethod.getStatusText());
                if (restMethod.getStatusCode() == 401) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Authentication to Zendesk failed.", zendeskCompositeSource));
                } else if (restMethod.getStatusCode() >= 500) {
                    throw new RuntimeException("Zendesk server error--please try again later.");
                } /*else if (restMethod.getStatusCode() == 404) {
                    throw new ReportException(new DataSourceConnectivityReportFault("The specified URL for your Zendesk account was not found.", zendeskCompositeSource));
                }*/
                Object o = new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
                if(o instanceof String) {
                    throw new RuntimeException((String) o);
                }
                results = (Map) o;
                restMethod.releaseConnection();
                successful = true;
            } catch (ReportException re) {
                throw re;
            } catch (Exception e) {
                LogClass.error(e);
                retryCount++;
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Zendesk could not be reached due to a large number of current users, please try again in a bit.");
        }

        return results;
    }

    public Document runRestRequest(ZendeskCompositeSource zendeskCompositeSource, HttpClient client, String path, Builder builder) throws InterruptedException {
        HttpMethod restMethod = new GetMethod(zendeskCompositeSource.getUrl() + path);
        String data;
        if (path.startsWith("/search") || path.startsWith("/api/v2"))
        {
            // add  Authorization header with base64 encoded "<username>:<password>"
            StringBuilder toEncode = new StringBuilder();
            toEncode.append(zendeskCompositeSource.getZdUserName()).append(":").append(zendeskCompositeSource.getZdPassword());
            byte[] bytes;
            try {
                bytes = toEncode.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            BASE64Encoder base64Encoder = new BASE64Encoder();
            restMethod.setRequestHeader("Authorization", "Basic " + base64Encoder.encode(bytes));
        }
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        boolean successful = false;
        Document doc = null;
        int retryCount = 0;
        do {
            try {
                client.executeMethod(restMethod);
                data = restMethod.getResponseBodyAsString();
                doc = builder.build(new ByteArrayInputStream(data.getBytes("UTF-8")));
//                doc = builder.build(restMethod.getResponseBodyAsStream());


                if (restMethod.getStatusCode() == 401) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Invalid Zendesk credentials in connecting to " +
                            zendeskCompositeSource.getUrl() + ".",
                            zendeskCompositeSource));
                } else if (restMethod.getStatusCode() == 404) {
                    throw new ReportException(new DataSourceConnectivityReportFault("No Zendesk system was found at " +
                            zendeskCompositeSource.getUrl() + ". If your Zendesk account is using domain mapping, please use the actual Zendesk URL instead of the mapped domain.",
                            zendeskCompositeSource));
                } else if (restMethod.getStatusCode() == 503) {
                    Thread.sleep(20000);
                    System.out.println("throttling, waiting 20 seconds...");
                    retryCount++;
                } else {
                    successful = true;
                }
            } catch (ReportException re) {
                throw re;
            } catch (Exception e) {
                if(restMethod == null || restMethod.getStatusLine() == null)
                    throw new RuntimeException(e);
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

    public Object runJSONRestRequest(ZendeskCompositeSource zendeskCompositeSource, HttpClient client, String path, Builder builder) throws InterruptedException {

        HttpMethod restMethod;
        if(path.startsWith("https://"))
            restMethod = new GetMethod(path);
        else
            restMethod = new GetMethod(zendeskCompositeSource.getUrl() + path);

        restMethod.setRequestHeader("Accept", "application/json");
        restMethod.setRequestHeader("Content-Type", "application/json");
        boolean successful = false;

        Object obj = null;
        int retryCount = 0;
        do {
            try {
                client.executeMethod(restMethod);
                obj = new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
                if (restMethod.getStatusCode() == 401) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Invalid Zendesk credentials in connecting to " +
                            zendeskCompositeSource.getUrl() + ".",
                            zendeskCompositeSource));
                } else if (restMethod.getStatusCode() == 404) {
                    throw new ReportException(new DataSourceConnectivityReportFault("No Zendesk system was found at " +
                            zendeskCompositeSource.getUrl() + ". If your Zendesk account is using domain mapping, please use the actual Zendesk URL instead of the mapped domain.",
                            zendeskCompositeSource));
                } else if (restMethod.getStatusCode() == 503) {
                    Thread.sleep(20000);
                    System.out.println("throttling, waiting 20 seconds...");
                    retryCount++;
                } else {
                    successful = true;
                }
            } catch (ReportException re) {
                throw re;
            } catch (Exception e) {
                if(restMethod == null || restMethod.getStatusLine() == null)
                    throw new RuntimeException(e);
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
        if(obj instanceof String) {
            throw new RuntimeException((String) obj);
        }
        return obj;
    }
}
