package com.easyinsight.datafeeds.trello;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.constantcontact.ConstantContactCompositeSource;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.HmacSha1MessageSigner;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * User: jamesboe
 * Date: 3/12/13
 * Time: 8:48 PM
 */
public abstract class TrelloBaseSource extends ServerDataSourceDefinition {
    public JSONArray runRequest(String url, org.apache.http.client.HttpClient httpClient, TrelloCompositeSource trelloCompositeSource) throws IOException, JSONException, OAuthExpectationFailedException, OAuthMessageSignerException, OAuthCommunicationException {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(TrelloCompositeSource.KEY, TrelloCompositeSource.SECRET_KEY);
        consumer.setMessageSigner(new HmacSha1MessageSigner());
        consumer.setTokenWithSecret(trelloCompositeSource.getTokenKey(), trelloCompositeSource.getTokenSecret());
        HttpGet httpRequest = new HttpGet(url);
        httpRequest.setHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        consumer.sign(httpRequest);

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            public String handleResponse(final HttpResponse response)
                    throws IOException {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }

                HttpEntity entity = response.getEntity();
                return entity == null ? null : EntityUtils.toString(entity, "UTF-8");
            }
        };

        String string = httpClient.execute(httpRequest, responseHandler);
        return new JSONArray(string);
    }

    public JSONObject runRequestForObject(String url, org.apache.http.client.HttpClient httpClient, TrelloCompositeSource trelloCompositeSource) throws IOException, JSONException, OAuthExpectationFailedException, OAuthMessageSignerException, OAuthCommunicationException {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(TrelloCompositeSource.KEY, TrelloCompositeSource.SECRET_KEY);
        consumer.setMessageSigner(new HmacSha1MessageSigner());
        consumer.setTokenWithSecret(trelloCompositeSource.getTokenKey(), trelloCompositeSource.getTokenSecret());
        HttpGet httpRequest = new HttpGet(url);
        httpRequest.setHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        consumer.sign(httpRequest);

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            public String handleResponse(final HttpResponse response)
                    throws IOException {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }

                HttpEntity entity = response.getEntity();
                return entity == null ? null : EntityUtils.toString(entity, "UTF-8");
            }
        };

        String string = httpClient.execute(httpRequest, responseHandler);
        return new JSONObject(string);
    }
}
