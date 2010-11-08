package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.HmacSha1MessageSigner;
import oauth.signpost.signature.PlainTextMessageSigner;
import oauth.signpost.signature.QueryStringSigningStrategy;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Oct 25, 2010
 * Time: 6:03:02 PM
 */
public abstract class ConstantContactBaseSource extends ServerDataSourceDefinition {
    
    public static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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

    protected Document query(String queryString, String tokenKey, String tokenSecretKey) {
        Builder builder = new Builder();
        try {
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ConstantContactCompositeSource.CONSUMER_KEY, ConstantContactCompositeSource.CONSUMER_SECRET);
            consumer.setMessageSigner(new HmacSha1MessageSigner());
            consumer.setTokenWithSecret(tokenKey, tokenSecretKey);
            HttpGet httpRequest = new HttpGet(queryString);
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            consumer.sign(httpRequest);

            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string = client.execute(httpRequest, responseHandler);
            string = string.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");
            string = string.replace("xmlns=\"http://ws.constantcontact.com/ns/1.0/\"", "");
            string = string.replace("xmlns=\"http://www.w3.org/2007/app\"", "");
            //System.out.println(string);
            return builder.build(new ByteArrayInputStream(string.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
