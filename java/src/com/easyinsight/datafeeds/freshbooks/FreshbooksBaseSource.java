package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.logging.LogClass;
import nu.xom.*;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.signature.PlainTextMessageSigner;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Jul 22, 2010
 * Time: 6:41:41 PM
 */
public abstract class FreshbooksBaseSource extends ServerDataSourceDefinition {
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

    protected Document query(String queryString, String requestXML, FreshbooksCompositeSource parentSource) throws SQLException {
        //HttpClient client = getHttpClient();
        Builder builder = new Builder();
        try {
            // url
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(FreshbooksCompositeSource.CONSUMER_KEY, FreshbooksCompositeSource.CONSUMER_SECRET);
            consumer.setMessageSigner(new PlainTextMessageSigner());
            consumer.setTokenWithSecret(parentSource.getTokenKey(), parentSource.getTokenSecretKey());
            HttpPost httpRequest = new HttpPost(parentSource.getUrl() + "/api/2.1/xml-in");
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            BasicHttpEntity entity = new BasicHttpEntity();
            byte[] content = ("<request method=\""+queryString+"\">"+requestXML+"</request>").getBytes();
            entity.setContent(new ByteArrayInputStream(content));
            entity.setContentLength(content.length);
            httpRequest.setEntity(entity);
            consumer.sign(httpRequest);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setContentCharset(params, "utf-8");
            org.apache.http.client.HttpClient client = new DefaultHttpClient(params);

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(final HttpResponse response)
                        throws IOException {
                    org.apache.http.StatusLine statusLine = response.getStatusLine();
                    if (statusLine.getStatusCode() >= 300) {
                        throw new HttpResponseException(statusLine.getStatusCode(),
                                statusLine.getReasonPhrase());
                    }

                    HttpEntity entity = response.getEntity();
                    return entity == null ? null : EntityUtils.toString(entity, "UTF-8");
                }
            };

            String string = client.execute(httpRequest, responseHandler);
            string = string.replace("xmlns=\"http://www.freshbooks.com/api/\"", "");
            return builder.build(new ByteArrayInputStream(string.getBytes("UTF-8")));
        } catch (HttpResponseException hre) {
            LogClass.error(hre);
            throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Freshbooks data.", parentSource));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void addValue(IRow row, String keyName, Object value, Map<String, Key> keys) {
        Key key = keys.get(keyName);
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
