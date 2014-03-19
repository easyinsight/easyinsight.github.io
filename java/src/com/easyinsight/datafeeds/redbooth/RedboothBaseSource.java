package com.easyinsight.datafeeds.redbooth;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.zendesk.ZendeskCompositeSource;
import com.easyinsight.logging.LogClass;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 2/19/14
 * Time: 7:09 PM
 */
public abstract class RedboothBaseSource extends ServerDataSourceDefinition {

    private transient DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss Z");
    private transient DateTimeFormatter altDF = DateTimeFormat.forPattern("yyyy-MM-dd");
    private transient DateTimeFormatter yetAnotherDF = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    protected Value getDate(Map n, String key) {
        if (df == null) {
            df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss Z");
        }
        String value = getJSONValue(n, key);
        if (value != null) {
            try {
                Date date = df.parseDateTime(value).toDate();
                return new DateValue(date);
            } catch (Exception e) {
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }

    protected Value getYetAnotherDate(Map n, String key) {
        if (yetAnotherDF == null) {
            yetAnotherDF = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        }
        String value = getJSONValue(n, key);
        if (value != null) {
            try {
                Date date = yetAnotherDF.parseDateTime(value).toDate();
                return new DateValue(date);
            } catch (Exception e) {
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }

    protected Value getAlt(Map n, String key) {
        if (altDF == null) {
            altDF = DateTimeFormat.forPattern("yyyy-MM-dd");
        }
        String value = getJSONValue(n, key);
        if (value != null) {
            try {
                Date date = altDF.parseDateTime(value).toDate();
                return new DateValue(date);
            } catch (Exception e) {
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }

    public static HttpClient getHttpClient(RedboothCompositeSource source) {
        return new HttpClient();
    }

    protected Object queryList(String queryString, RedboothCompositeSource redboothCompositeSource, HttpClient client) {
        String url = "https://redbooth.com" + queryString;
        if (url.contains("?")) {
            url += ("&access_token=" + redboothCompositeSource.getAccessToken());
        } else {
            url += "?access_token=" + redboothCompositeSource.getAccessToken();
        }
        HttpMethod restMethod = new GetMethod(url);
        restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
        boolean successful = false;
        int retryCount = 0;
        Object results = null;
        do {
            try {
                client.executeMethod(restMethod);
                if (restMethod.getStatusCode() == 401) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Authentication to Redbooth failed.", redboothCompositeSource));
                } else if (restMethod.getStatusCode() >= 500) {
                    throw new RuntimeException("Redbooth server error--please try again later.");
                }
                Object o = new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
                if(o instanceof String) {
                    throw new RuntimeException((String) o);
                }
                results = o;
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
            throw new RuntimeException("Redbooth could not be reached due to a large number of current users, please try again in a bit.");
        }

        return results;
    }
}
