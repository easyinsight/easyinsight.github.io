package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 3/26/12
 * Time: 11:06 AM
 */
public abstract class BasecampNextBaseSource extends ServerDataSourceDefinition {

    protected JSONArray runJSONRequest(String path, BasecampNextCompositeSource parentDefinition) {
        return runJSONRequest(path, parentDefinition, null);
    }

    protected JSONArray runJSONRequest(String path, BasecampNextCompositeSource parentDefinition, @Nullable Date lastRefreshDate) {
        HttpClient client = new HttpClient();
        DateFormat df = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss Z");
        HttpMethod restMethod = new GetMethod("https://basecamp.com/"+parentDefinition.getEndpoint()+"/api/v1/" + path);

        restMethod.setRequestHeader("Authorization", "Bearer " + parentDefinition.getAccessToken());
        restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
        /*if (lastRefreshDate != null && lastRefreshDate.getTime() >= 100) {
            restMethod.setRequestHeader("If-None-Match", df.format(lastRefreshDate));
            restMethod.setRequestHeader("If-Modified-Since", df.format(lastRefreshDate));
        }*/
        boolean successful = false;
        JSONArray jsonObject = null;
        int retryCount = 0;
        do {
            try {
                client.executeMethod(restMethod);
                if (lastRefreshDate != null && lastRefreshDate.getTime() >= 100) {
                    
                    Header header = restMethod.getResponseHeader("Last-Modified");
                    if (header != null) {
                        Date lastDate = df.parse(header.getValue());
                        long delta = lastRefreshDate.getTime() - lastDate.getTime();
                        long daysSinceChange = delta / (60 * 60 * 1000 * 24);
                        if (daysSinceChange > 2) {
                            return null;
                        }
                        // Thu, 29 Mar 2012 20:18:58 GMT
                    }
                    //System.out.println("argh");
                }
                //System.out.println(restMethod.getResponseBodyAsString());
                jsonObject = new JSONArray(restMethod.getResponseBodyAsString());
                successful = true;
            } catch (IOException e) {
                retryCount++;
                if (e.getMessage().contains("429") || e instanceof SocketException) {
                    //noinspection EmptyCatchBlock
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e1) {
                    }
                } else {
                    throw new RuntimeException(e);
                }
            } catch (ReportException re) {
                throw re;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Basecamp could not be reached due to a large number of current users, please try again in a bit.");
        }
        return jsonObject;
    }

    public static void main(String[] args) throws ParseException {
        DateFormat df = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss Z");
        System.out.println(df.parse("Thu, 29 Mar 2012 20:18:58 GMT"));
    }

    protected JSONObject runJSONRequestForObject(String path, BasecampNextCompositeSource parentDefinition) {
        HttpClient client = new HttpClient();
        HttpMethod restMethod = new GetMethod("https://basecamp.com/"+parentDefinition.getEndpoint()+"/api/v1/" + path);

        restMethod.setRequestHeader("Authorization", "Bearer " + parentDefinition.getAccessToken());
        restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
        boolean successful = false;
        JSONObject jsonObject = null;
        int retryCount = 0;
        do {
            try {
                client.executeMethod(restMethod);
                jsonObject = new JSONObject(restMethod.getResponseBodyAsString());
                successful = true;
            } catch (IOException e) {
                System.out.println("IOException " + e.getMessage());
                retryCount++;
                if (e.getMessage().contains("429") || e instanceof SocketException) {
                    //noinspection EmptyCatchBlock
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e1) {
                    }
                } else {
                    throw new RuntimeException(e);
                }
            } catch (ReportException re) {
                throw re;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Basecamp could not be reached due to a large number of current users, please try again in a bit.");
        }
        return jsonObject;
    }

    protected JSONObject rawJSONRequestForObject(String path, BasecampNextCompositeSource parentDefinition) {
        HttpClient client = new HttpClient();
        HttpMethod restMethod = new GetMethod(path);

        restMethod.setRequestHeader("Authorization", "Bearer " + parentDefinition.getAccessToken());
        restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
        boolean successful = false;
        JSONObject jsonObject = null;
        int retryCount = 0;
        do {
            try {
                client.executeMethod(restMethod);
                jsonObject = new JSONObject(restMethod.getResponseBodyAsString());
                successful = true;
            } catch (IOException e) {
                System.out.println("IOException " + e.getMessage());
                retryCount++;
                if (e.getMessage().contains("429") || e instanceof SocketException) {
                    //noinspection EmptyCatchBlock
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e1) {
                    }
                } else {
                    throw new RuntimeException(e);
                }
            } catch (ReportException re) {
                throw re;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Basecamp could not be reached due to a large number of current users, please try again in a bit.");
        }
        return jsonObject;
    }
}
