package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
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
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/10/14
 * Time: 11:45 AM
 */
public abstract class FreshdeskBaseSource extends ServerDataSourceDefinition {
    protected static HttpClient getHttpClient(String username) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, "X");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    protected static String getValue(Map n, String key) {
        Object obj = n.get(key);
        if(obj != null)
            return obj.toString();
        else
            return null;
    }

    private transient DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    protected Value getDate(Map n, String key) {
        if (df == null) {
            df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        }
        String value = getValue(n, key);
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

    protected static Map runRestRequestForMap(String path, HttpClient client, FreshdeskCompositeSource parentDefinition) throws ReportException {
        String url = parentDefinition.getUrl() + "/helpdesk/";
        HttpMethod restMethod = new GetMethod(url + path);
        restMethod.setRequestHeader("Accept", "application/json");
        restMethod.setRequestHeader("Content-Type", "application/json");

        try {
            client.executeMethod(restMethod);
            if (restMethod.getStatusCode() == 404) {
                throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Freshdesk instance at " + url, parentDefinition));
            } else if (restMethod.getStatusCode() == 401) {
                throw new ReportException(new DataSourceConnectivityReportFault("Your API key was invalid.", parentDefinition));
            }
            return (Map) new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static List runRestRequestForList(String path, HttpClient client, FreshdeskCompositeSource parentDefinition) throws ReportException {
        String url = parentDefinition.generateURL(parentDefinition.getUrl(), "freshdesk.com") + "/helpdesk/";
        HttpMethod restMethod = new GetMethod(url + path);
        restMethod.setRequestHeader("Accept", "application/json");
        restMethod.setRequestHeader("Content-Type", "application/json");

        try {
            client.executeMethod(restMethod);
            if (restMethod.getStatusCode() == 404) {
                throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Freshdesk instance at " + url, parentDefinition));
            } else if (restMethod.getStatusCode() == 401) {
                throw new ReportException(new DataSourceConnectivityReportFault("Your API key was invalid.", parentDefinition));
            }
            Object o = new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
            return (List) o;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
