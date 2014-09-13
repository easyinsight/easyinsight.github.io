package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.logging.LogClass;
import net.minidev.json.parser.JSONParser;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 7/16/12
 * Time: 9:31 AM
 */
public abstract class Batchbook2BaseSource extends ServerDataSourceDefinition {
    protected static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        /*client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);*/
        return client;
    }

    protected static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    protected static String getValue(Map n, String key) {
        Object obj = n.get(key);
        if(obj != null)
            return obj.toString();
        else
            return null;
    }

    protected Date getDate(Map map, String value, SimpleDateFormat dateFormat) {
        Object obj = map.get(value);
        if (obj == null) {
            return null;
        }
        String string = obj.toString();
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            LogClass.error(e);
            return null;
        }
    }

    protected static Map runRestRequest(String path, HttpClient client, Batchbook2CompositeSource parentDefinition) throws ReportException {
        String url = parentDefinition.getUrl() + "/api/v1";
        String getURL = url + path + (path.contains("?") ? "&" : "?") + "auth_token=" + parentDefinition.getToken();
        HttpMethod restMethod = new GetMethod(getURL);
        restMethod.setRequestHeader("Accept", "application/json");
        restMethod.setRequestHeader("Content-Type", "application/json");

        try {
            client.executeMethod(restMethod);
            if (restMethod.getStatusCode() == 404) {
                throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Batchbook instance at " + url, parentDefinition));
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

    protected static Map runV2RestRequest(String path, HttpClient client, Batchbook2CompositeSource parentDefinition) throws ReportException {
        String url = parentDefinition.getUrl() + "/api/v2";
        String blah = url + path + (path.contains("?") ? "&" : "?") + "auth_token=" + parentDefinition.getToken();
        HttpMethod restMethod = new GetMethod(blah);
        restMethod.setRequestHeader("Accept", "application/json");
        restMethod.setRequestHeader("Content-Type", "application/json");

        try {
            client.executeMethod(restMethod);
            if (restMethod.getStatusCode() == 404) {
                throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Batchbook instance at " + url, parentDefinition));
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
}
