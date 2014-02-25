package com.easyinsight.datafeeds.surveygizmo;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp3.CommonsHttp3OAuthConsumer;
import oauth.signpost.signature.HmacSha1MessageSigner;
import oauth.signpost.signature.PlainTextMessageSigner;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 2/20/14
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class SurveyGizmoUtils {

    public static JSONObject runRequest(String url, HttpClient client, SurveyGizmoCompositeSource compositeConnection, List<NameValuePair> params) {
        String target = "https://restapi.surveygizmo.com/v2" + url;
        HttpMethod restMethod = new GetMethod(target);
        OAuthConsumer consumer = new CommonsHttp3OAuthConsumer(SurveyGizmoCompositeSource.CONSUMER_KEY, SurveyGizmoCompositeSource.CONSUMER_SECRET);
        consumer.setMessageSigner(new HmacSha1MessageSigner());
        consumer.setTokenWithSecret(compositeConnection.getSgToken(), compositeConnection.getSgSecret());
        restMethod.setQueryString(params.toArray(new NameValuePair[]{}));
        restMethod.setRequestHeader("Accept", "application/json");
        restMethod.setRequestHeader("Content-Type", "application/json");

        try {
            consumer.sign(restMethod);
            client.executeMethod(restMethod);
            if (restMethod.getStatusCode() == 401) {
                throw new ReportException(new DataSourceConnectivityReportFault("Your API key was invalid.", compositeConnection));
            }
            Object o = new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
            System.out.println(o);
            return (JSONObject) o;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
