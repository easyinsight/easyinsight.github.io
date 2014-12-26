package com.easyinsight.datafeeds.trello;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.HmacSha1MessageSigner;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/12/13
 * Time: 8:48 PM
 */
public abstract class TrelloBaseSource extends ServerDataSourceDefinition {

    protected static Date getDate(Map n, String key, SimpleDateFormat simpleDateFormat) throws java.text.ParseException {
        Object obj = n.get(key);
        if(obj != null)
            return simpleDateFormat.parse(obj.toString());
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

    public JSONArray runRequest(String url, org.apache.http.client.HttpClient httpClient, TrelloCompositeSource trelloCompositeSource) throws IOException, OAuthExpectationFailedException, OAuthMessageSignerException, OAuthCommunicationException {
        int retryCount = 0;
        do {
            try {
                OAuthConsumer consumer = new CommonsHttpOAuthConsumer(TrelloCompositeSource.KEY, TrelloCompositeSource.SECRET_KEY);
                consumer.setMessageSigner(new HmacSha1MessageSigner());
                consumer.setTokenWithSecret(trelloCompositeSource.getTokenKey(), trelloCompositeSource.getTokenSecret());
                HttpGet httpRequest = new HttpGet(url);
                httpRequest.setHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
                consumer.sign(httpRequest);

                ResponseHandler<String> responseHandler = response -> {
                    StatusLine statusLine = response.getStatusLine();
                    if (statusLine.getStatusCode() >= 300) {
                        throw new HttpResponseException(statusLine.getStatusCode(),
                                statusLine.getReasonPhrase());
                    }

                    HttpEntity entity = response.getEntity();
                    return entity == null ? null : EntityUtils.toString(entity, "UTF-8");
                };

                String string = httpClient.execute(httpRequest, responseHandler);
                ByteArrayInputStream bais = new ByteArrayInputStream(string.getBytes());
                return (JSONArray) new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(bais);
            } catch (HttpResponseException hre) {
                if (hre.getMessage().contains("Time-out")) {
                    retryCount++;
                } else {
                    throw new RuntimeException(hre);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } while (retryCount < 3);
        throw new RuntimeException("Trello server error--please try again later.");
    }

    public JSONObject runRequestForObject(String url, org.apache.http.client.HttpClient httpClient, TrelloCompositeSource trelloCompositeSource) throws IOException, OAuthExpectationFailedException, OAuthMessageSignerException, OAuthCommunicationException {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(TrelloCompositeSource.KEY, TrelloCompositeSource.SECRET_KEY);
        consumer.setMessageSigner(new HmacSha1MessageSigner());
        consumer.setTokenWithSecret(trelloCompositeSource.getTokenKey(), trelloCompositeSource.getTokenSecret());
        HttpGet httpRequest = new HttpGet(url);
        httpRequest.setHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        consumer.sign(httpRequest);

        ResponseHandler<String> responseHandler = response -> {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() >= 300) {
                throw new HttpResponseException(statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }

            HttpEntity entity = response.getEntity();
            return entity == null ? null : EntityUtils.toString(entity, "UTF-8");
        };

        String string = httpClient.execute(httpRequest, responseHandler);
        ByteArrayInputStream bais = new ByteArrayInputStream(string.getBytes());
        try {
            return (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(bais);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
