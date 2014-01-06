package com.easyinsight.datafeeds.github;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/5/14
 * Time: 11:19 AM
 */
public abstract class GithubBaseSource extends ServerDataSourceDefinition {
    protected List queryList(String queryString, GithubCompositeSource githubCompositeSource, HttpClient client) {
        HttpMethod restMethod = new GetMethod("https://api.github.com/" + queryString + "?access_token="+ githubCompositeSource.getAccessToken());
        List results = null;
        boolean successful = false;
        do {
            try {
                client.executeMethod(restMethod);
                if (restMethod.getStatusCode() == 401) {
                    throw new ReportException(new DataSourceConnectivityReportFault("", githubCompositeSource));
                }
                if (restMethod.getStatusCode() == 403) {
                    // handle
                }
                Object o = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
                if(o instanceof String) {
                    throw new RuntimeException((String) o);
                }
                results = (List) o;
                restMethod.releaseConnection();
                successful = true;
            } catch (ReportException re) {
                throw re;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } while (!successful);
        return results;
    }
}
