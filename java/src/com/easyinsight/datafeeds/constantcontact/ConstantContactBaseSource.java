package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import nu.xom.*;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.HmacSha1MessageSigner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

    protected Document query(String queryString, String tokenKey, String tokenSecretKey, FeedDefinition parentSource) throws OAuthExpectationFailedException, OAuthMessageSignerException, OAuthCommunicationException, IOException, ParsingException {
        try {
            Builder builder = new Builder();
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ConstantContactCompositeSource.CONSUMER_KEY, ConstantContactCompositeSource.CONSUMER_SECRET);
            consumer.setMessageSigner(new HmacSha1MessageSigner());
            consumer.setTokenWithSecret(tokenKey, tokenSecretKey);
            HttpGet httpRequest = new HttpGet(queryString);
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            consumer.sign(httpRequest);

            org.apache.http.client.HttpClient client = new DefaultHttpClient();
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

            String string = client.execute(httpRequest, responseHandler);
            string = string.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");
            string = string.replace("xmlns=\"http://ws.constantcontact.com/ns/1.0/\"", "");
            string = string.replace("xmlns=\"http://www.w3.org/2007/app\"", "");
            System.out.println(string);
            return builder.build(new ByteArrayInputStream(string.getBytes("UTF-8")));
        } catch (HttpResponseException e) {
            if ("Unauthorized".equals(e.getMessage())) {
                throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Constant Contact data.", parentSource));
            } else if ("Bad Request".equals(e.getMessage())) {
                throw new RuntimeException("Bad request on query of " + queryString);
            } else {
                throw e;
            }
        }
    }
}
