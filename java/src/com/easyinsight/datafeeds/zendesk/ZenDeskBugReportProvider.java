package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.logging.LogClass;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringEscapeUtils;
import org.hamcrest.Description;

import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 24, 2009
 * Time: 11:35:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ZenDeskBugReportProvider {

    public static DiskFileItemFactory uploadFactory;

    static {
        uploadFactory = new DiskFileItemFactory();
        uploadFactory.setSizeThreshold(4000000);
    }

    private static final String ZENDESK_API_USERNAME = "jboe@easy-insight.com";
    private static final String ZENDESK_API_PASSWORD = "e@symone$";
    private static final String ZENDESK_API_ENDPOINT = "https://easyinsight.zendesk.com/tickets.xml";
    private static final String ZENDESK_ATTACHMENT_API_ENDPOINT = "https://easyinsight.zendesk.com/uploads.xml?filename={0}";

    private static final String ZENDESK_TICKET_XML = "<ticket>\n" +
            "  <subject>{0}</subject>\n" +
                    "  <description>{1}</description>\n" +
                    "  <requester-email>{2}</requester-email>\n" +
                    "  <ticket-field-entries type=\"array\">\n" +
                    " {3}{4}" +
                    "  </ticket-field-entries>\n" +
                    " {5}" +
                    "</ticket>";
    private static final String ZENDESK_REPORT_TYPE_XML = "<ticket-field-entry>\n" +
            "      <ticket-field-id>20386816</ticket-field-id>\n" +
            "      <value>{0}</value>\n" +
            "    </ticket-field-entry>";

    private static final String ZENDESK_CONNECTION_XML = "<ticket-field-entry>\n" +
            "      <ticket-field-id>20386776</ticket-field-id>\n" +
            "      <value>{0}</value>\n" +
            "    </ticket-field-entry>";

    private static final String ZENDESK_UPLOAD_XML = "<uploads>{0}</uploads>";

    private static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    public void reportBug(String subject, String description, String email, String reportType, String connectionName, String attachment) {
        try {
            LogClass.error(attachment);
            HttpClient client = getHttpClient(ZENDESK_API_USERNAME, ZENDESK_API_PASSWORD);
            PostMethod method = new PostMethod(ZENDESK_API_ENDPOINT);
            String reportTypeXml = "";
            String connectionXml = "";
            String uploadXml = "";
            if(!attachment.isEmpty())
                uploadXml = MessageFormat.format(ZENDESK_UPLOAD_XML, attachment);
            if(!reportType.isEmpty())
                reportTypeXml = MessageFormat.format(ZENDESK_REPORT_TYPE_XML, reportType);
            if(!connectionName.isEmpty())
                connectionXml = MessageFormat.format(ZENDESK_CONNECTION_XML, connectionName);
            String content = MessageFormat.format(ZENDESK_TICKET_XML, subject, description, email, reportTypeXml,
                    connectionXml, uploadXml);
            StringRequestEntity entity = new StringRequestEntity(content, "text/xml", "UTF-8");
            method.setRequestEntity(entity);
            client.executeMethod(method);
            String s = method.getResponseBodyAsString();
            if(s.length() > 1) {
                throw new Exception(s);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public String uploadAttachment(byte[] attachment, String fileName, String token) {
        try {
            HttpClient client = getHttpClient(ZENDESK_API_USERNAME, ZENDESK_API_PASSWORD);
            String tokenParam = "";
            if(token != null && token.length() > 0) {
                token = "&token=" + token;
            }
            PostMethod method = new PostMethod(MessageFormat.format(ZENDESK_ATTACHMENT_API_ENDPOINT + tokenParam, fileName));
            ByteArrayRequestEntity requestEntity = new ByteArrayRequestEntity(attachment, "application/binary");
            method.setRequestEntity(requestEntity);
            client.executeMethod(method);
            String s = method.getResponseBodyAsString();
            int loc = s.indexOf("token=\"");
            if(loc > 0) {
                int secondQuote = s.indexOf("\"", loc + 7);
                token = s.substring(loc + 7, secondQuote);
            } else {
                throw new Exception(s);
            }

            return token;
        } catch(Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
