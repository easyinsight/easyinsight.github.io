package com.easyinsight.users;

import com.easyinsight.logging.LogClass;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * User: jamesboe
 * Date: 3/28/11
 * Time: 7:12 PM
 */
@Entity
@Table(name="quickbase_external_login")
@PrimaryKeyJoinColumn(name="external_login_id")
public class QuickbaseExternalLogin extends ExternalLogin {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="quickbase_external_login_id")
    private long quickbaseExternalLoginID;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public long getQuickbaseExternalLoginID() {
        return quickbaseExternalLoginID;
    }

    public void setQuickbaseExternalLoginID(long quickbaseExternalLoginID) {
        this.quickbaseExternalLoginID = quickbaseExternalLoginID;
    }

    private static final String AUTHENTICATE_XML = "<username>{0}</username><password>{1}</password><hours>144</hours>";

    @Column(name="host_name")
    private String hostName;

    public String login(String userName, String password) {
        try {
            String requestBody = MessageFormat.format(AUTHENTICATE_XML, userName, password);
            Document doc = executeRequest(hostName, null, "API_Authenticate", requestBody);
            String errorCode = doc.query("/qdbapi/errcode/text()").get(0).getValue();
            if ("0".equals(errorCode)) {
                //return doc.query("/qdbapi/ticket/text()").get(0).getValue();
                return null;
            } else {
                return doc.query("/qdbapi/errtext/text()").get(0).getValue();
            }
        } catch (Exception e) {
            LogClass.error(e);
            return e.getMessage();
        }
    }

    private Document executeRequest(String host, String path, String action, String requestBody) throws IOException, ParsingException {
        if (path == null) {
            path = "main";
        }
        String fullPath = "https://" + host + "/db/" + path;
        HttpPost httpRequest = new HttpPost(fullPath);
        httpRequest.setHeader("Accept", "application/xml");
        httpRequest.setHeader("Content-Type", "application/xml");
        httpRequest.setHeader("QUICKBASE-ACTION", action);
        BasicHttpEntity entity = new BasicHttpEntity();
        String contentString = "<qdbapi>"+requestBody+"</qdbapi>";
        byte[] contentBytes = contentString.getBytes();
        entity.setContent(new ByteArrayInputStream(contentBytes));
        entity.setContentLength(contentBytes.length);
        httpRequest.setEntity(entity);
        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        String string = client.execute(httpRequest, responseHandler);
        return new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));
    }
}
