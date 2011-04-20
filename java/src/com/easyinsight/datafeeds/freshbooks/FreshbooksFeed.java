package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.logging.LogClass;
import nu.xom.*;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.PlainTextMessageSigner;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Jul 29, 2010
 * Time: 10:07:03 AM
 */
public abstract class FreshbooksFeed extends Feed {

    private String url;
    private String tokenKey;
    private String tokenSecretKey;

    protected FreshbooksFeed(String url, String tokenKey, String tokenSecretKey) {
        this.url = url;
        this.tokenKey = tokenKey;
        this.tokenSecretKey = tokenSecretKey;
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

    protected Document query(String queryString, String requestXML, EIConnection conn) throws SQLException {
        //HttpClient client = getHttpClient();
        Builder builder = new Builder();
        try {
            // url
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(FreshbooksCompositeSource.CONSUMER_KEY, FreshbooksCompositeSource.CONSUMER_SECRET);
            consumer.setMessageSigner(new PlainTextMessageSigner());
            consumer.setTokenWithSecret(tokenKey, tokenSecretKey);
            HttpPost httpRequest = new HttpPost(url + "/api/2.1/xml-in");
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            BasicHttpEntity entity = new BasicHttpEntity();
            byte[] content = ("<request method=\""+queryString+"\">"+requestXML+"</request>").getBytes();
            entity.setContent(new ByteArrayInputStream(content));
            entity.setContentLength(content.length);
            httpRequest.setEntity(entity);
            consumer.sign(httpRequest);
            
            HttpClient client = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string = client.execute(httpRequest, responseHandler);
            string = string.replace("xmlns=\"http://www.freshbooks.com/api/\"", "");            
            return builder.build(new ByteArrayInputStream(string.getBytes("UTF-8")));
        } catch (HttpResponseException hre) {
            LogClass.error(hre);
            throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Freshbooks data.", getParentSource(conn)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void addValue(IRow row, String keyName, Object value, Map<String, Collection<Key>> keys) {
        Collection<Key> keyColl = keys.get(keyName);
        if (keyColl != null) {
            for (Key key : keyColl) {
                if (value instanceof String) {
                    row.addValue(key, (String) value);
                } else if (value instanceof Date) {
                    row.addValue(key, (Date) value);
                } else if (value instanceof Number) {
                    row.addValue(key, (Number) value);
                } else if (value instanceof Value) {
                    row.addValue(key, (Value) value);
                } else if (value == null) {

                } else {
                    throw new RuntimeException();
                }
            }
        }
    }
}
