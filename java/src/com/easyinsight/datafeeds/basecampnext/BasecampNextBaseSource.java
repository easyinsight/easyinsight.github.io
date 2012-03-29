package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;

/**
 * User: jamesboe
 * Date: 3/26/12
 * Time: 11:06 AM
 */
public abstract class BasecampNextBaseSource extends ServerDataSourceDefinition {
    protected JSONArray runJSONRequest(String path, BasecampNextCompositeSource parentDefinition) {
        HttpClient client = new HttpClient();
        HttpMethod restMethod = new GetMethod("https://basecamp.com/"+parentDefinition.getEndpoint()+"/api/v1/" + path);

        restMethod.setRequestHeader("Authorization", "Bearer " + parentDefinition.getAccessToken());
        restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
        boolean successful = false;
        JSONArray jsonObject = null;
        int retryCount = 0;
        do {
            try {
                client.executeMethod(restMethod);
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
