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
        System.out.println(string);
        return new JSONArray(string);
    }

    public static void main(String[] args) throws Exception {
        HttpClient httpClient = new HttpClient();
        String key = "5d049b71083c27518166c0f0a598d067";
        HttpMethod restMethod = new GetMethod("https://api.trello.com/1/members/me/boards?key=" + key + "&token=" + "850799af2e6d30b08e2f55deeb3bc4ffccb7d9147c3bb77c8a0a589508a7bd71");

        restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");

        httpClient.executeMethod(restMethod);
        System.out.println(restMethod.getResponseBodyAsString() );
        JSONArray boards = new JSONArray(restMethod.getResponseBodyAsString());
        for (int i = 0 ; i < boards.length(); i++) {
            JSONObject board = (JSONObject) boards.get(i);
            System.out.println(board.get("id") + " - " + board.get("name") + " - " + board.get("description") + " - " + board.get("url"));
            String id = (String) board.get("id");
            restMethod = new GetMethod("https://api.trello.com/1/boards/" + id + "/cards?key=" + key + "&token=" + "850799af2e6d30b08e2f55deeb3bc4ffccb7d9147c3bb77c8a0a589508a7bd71");
            restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
            restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
            httpClient.executeMethod(restMethod);
            System.out.println(restMethod.getResponseBodyAsString() );
            JSONArray cards = new JSONArray(restMethod.getResponseBodyAsString());
            for (int j = 0; j < cards.length(); j++) {
                JSONObject cardObj = (JSONObject) cards.get(j);
                System.out.println("\t" + cardObj.get("id") + " - "+ cardObj.get("name") + " - " + cardObj.get("idBoard") + " - " + cardObj.get("idList"));
            }
            restMethod = new GetMethod("https://api.trello.com/1/boards/" + id + "/lists?key=" + key + "&token=" + "850799af2e6d30b08e2f55deeb3bc4ffccb7d9147c3bb77c8a0a589508a7bd71") ;
            restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
            restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
            httpClient.executeMethod(restMethod);
            System.out.println(restMethod.getResponseBodyAsString() );
        }
        //restMethod.releaseConnection();
    }
}
