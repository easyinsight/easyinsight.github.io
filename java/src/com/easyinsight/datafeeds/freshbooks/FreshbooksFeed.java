package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.datafeeds.Feed;
import nu.xom.*;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.PlainTextMessageSigner;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;

/**
 * User: jamesboe
 * Date: Jul 29, 2010
 * Time: 10:07:03 AM
 */
public abstract class FreshbooksFeed extends Feed {

    private String url;
    private String tokenKey;
    private String tokenSecretKey;

    public static final String CONSUMER_KEY = "easyinsight";
    public static final String CONSUMER_SECRET = "3gKm7ivgkPCeQZChh7ig9CDMBGratLg6yS";

    protected FreshbooksFeed(String url, String tokenKey, String tokenSecretKey) {
        this.url = url;
        this.tokenKey = tokenKey;
        this.tokenSecretKey = tokenSecretKey;
    }

    protected String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    protected Document query(String queryString, String requestXML) {
        //HttpClient client = getHttpClient();
        Builder builder = new Builder();
        try {
            // url
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
            consumer.setMessageSigner(new PlainTextMessageSigner());
            consumer.setTokenWithSecret(tokenKey, tokenSecretKey);
            HttpPost httpRequest = new HttpPost(url + "/api/2.1/xml-in");
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            /*PostMethod post = new PostMethod(url + "/api/2.1/xml-in");
            post.setRequestHeader("Accept", "application/xml");
            post.setRequestHeader("Content-Type", "application/xml");*/
            BasicHttpEntity entity = new BasicHttpEntity();
            entity.setContent(new ByteArrayInputStream(("<request method=\""+queryString+"\">"+requestXML+"</request>").getBytes()));
            httpRequest.setEntity(entity);
            //post.setRequestBody();
            HttpRequest signedRequest = consumer.sign(httpRequest);
            
            HttpClient client = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string = client.execute(httpRequest, responseHandler);
            //client.executeMethod(post);
            //response.get
            //String string = post.getResponseBodyAsString();
            string = string.replace("xmlns=\"http://www.freshbooks.com/api/\"", "");            
            return builder.build(new ByteArrayInputStream(string.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
