package test.quickbooks;

import junit.framework.TestCase;
import com.easyinsight.PasswordStorage;
import com.easyinsight.database.EIConnection;
import com.easyinsight.database.Database;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.contrib.ssl.AuthSSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 16, 2009
 * Time: 3:00:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuickbooksDataSourceTest extends TestCase {
    public void testGetTicket() {
        Database.initialize();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            String sessionTicket = PasswordStorage.getSessionTicket(451L, conn);
            DateFormat df = new SimpleDateFormat("yyyyMMdd'T'hhmmss");
            String clientDate = df.format(new Date());
            String stupidPost = "<!DOCTYPE QBXML PUBLIC '-//INTUIT//DTD QBXML QBO 2.0//EN' 'http://apps.quickbooks.com/dtds/qbxmlops20.dtd'>\n";
            stupidPost += "<QBXML>";
            stupidPost += "<SignonMsgsRq><SignonAppCertRq><ClientDateTime>" + clientDate + "</ClientDateTime><ApplicationLogin>easy-insight.www.easy-insight.com</ApplicationLogin><ConnectionTicket>" + sessionTicket + "</ConnectionTicket><Language>English</Language><AppID>151497234</AppID><AppVer>1.0</AppVer></SignonAppCertRq></SignonMsgsRq>";
            stupidPost += "</QBXML>";
            HttpClient client = new HttpClient();
            Protocol.registerProtocol("qbhttps", new Protocol("qbhttps", (ProtocolSocketFactory) new AuthSSLProtocolSocketFactory(QuickbooksDataSourceTest.class.getClassLoader().getResource("intuit-keystore.jks"), "dmskey", QuickbooksDataSourceTest.class.getClassLoader().getResource("intuit-truststore.jks"), "dmskey"), 443));
            PostMethod method = new PostMethod("qbhttps://webapps.quickbooks.com/j/AppGateway");
            
            RequestEntity entity = null;
            entity = new StringRequestEntity(stupidPost, "application/x-qbxml", null);
            method.setRequestEntity(entity);
            client.executeMethod(method);
            conn.commit();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        }
        catch(SQLException ex) {
            conn.rollback();
            throw new RuntimeException(ex);
        }
        catch (HttpException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            Database.closeConnection(conn);
        }
    }
}
