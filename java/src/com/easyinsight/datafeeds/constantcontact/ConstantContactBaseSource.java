package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import nu.xom.*;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Oct 25, 2010
 * Time: 6:03:02 PM
 */
public abstract class ConstantContactBaseSource extends ServerDataSourceDefinition {
    
    public static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    protected String queryField(Map n, String xpath) {
        Object result = n.get(xpath);
        if (result == null) {
            return "";
        } else {
            return result.toString();
        }
    }

    protected Date queryDate(Map n, String xpath) {
        Object result = n.get(xpath);
        if (result == null) {
            return null;
        } else {
            String string = result.toString();
            try {
                return DATE_FORMAT.parse(string);
            } catch (java.text.ParseException e) {
                return null;
            }
        }
    }

    protected String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0) {
            String str = results.get(0).getValue();
            try {
                int num = Integer.parseInt(str);
                return String.valueOf(num);
            } catch (NumberFormatException e) {
                return str;
            }
        } else
            return null;
    }

    protected Map query(String queryString, FeedDefinition parentSource, HttpClient client) throws OAuthExpectationFailedException, OAuthMessageSignerException, OAuthCommunicationException, IOException, ParsingException {
        try {
            System.out.println(queryString);
            ConstantContactCompositeSource parentDefinition = (ConstantContactCompositeSource) parentSource;
            HttpMethod restMethod = new GetMethod(queryString);
            restMethod.setRequestHeader("Authorization", "Bearer " + parentDefinition.getAccessToken());
            restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
            restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
            client.executeMethod(restMethod);
            //System.out.println(restMethod.getResponseBodyAsString());
            if (restMethod.getStatusCode() == 401) {
                throw new ReportException(new DataSourceConnectivityReportFault("Authentication to Constant Contact failed.", parentSource));
            } else if (restMethod.getStatusCode() == 500) {
                throw new RuntimeException("Constant Contact server error--please try again later.");
            }
            Map results = (Map) new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
            restMethod.releaseConnection();
            return results;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected List queryList(String queryString, FeedDefinition parentSource, HttpClient client) throws OAuthExpectationFailedException, OAuthMessageSignerException, OAuthCommunicationException, IOException, ParsingException {
        try {
            //System.out.println(queryString);
            ConstantContactCompositeSource parentDefinition = (ConstantContactCompositeSource) parentSource;
            HttpMethod restMethod = new GetMethod(queryString);
            restMethod.setRequestHeader("Authorization", "Bearer " + parentDefinition.getAccessToken());
            restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
            restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
            client.executeMethod(restMethod);
            if (restMethod.getStatusCode() == 401) {
                throw new ReportException(new DataSourceConnectivityReportFault("Authentication to Constant Contact failed.", parentSource));
            } else if (restMethod.getStatusCode() == 500) {
                throw new RuntimeException("Constant Contact server error--please try again later.");
            }
            List results = (List) new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
            restMethod.releaseConnection();
            return results;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
