package com.easyinsight.datafeeds.wesabe;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.users.Credentials;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * User: jboe
 * Date: Jul 16, 2009
 * Time: 9:48:08 AM
 */
public abstract class WesabeBaseSource extends ServerDataSourceDefinition {
    public static final String ACCOUNT_ID = "Account ID";

    protected NodeList getNodes(Credentials credentials, String xml, String baseTag) throws Exception {
        String client_id = "EasyInsight";  // Change this to the name of your application
        String client_version = "1.0";          // Give your application a version number
        String api_version = "1.0.0";        // Document which API version you're using

        Properties sys = System.getProperties();
        String system_name = sys.getProperty("os.name");
        String system_release = sys.getProperty("os.version");

        String userAgent = client_id + "/" + client_version +
                " (" + system_name + " " + system_release + ")" +
                " Wesabe-API/" + api_version;
        String credentialString = credentials.getUserName() + ":" + credentials.getPassword();
        String encoding = new sun.misc.BASE64Encoder().encode(credentialString.getBytes());
        URL wesabe = new URL("https://www.wesabe.com/" +xml + ".xml");
        URLConnection connection = wesabe.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + encoding);
        connection.setRequestProperty("User-Agent", userAgent);
        InputStream content = connection.getInputStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(content);
        return document.getElementsByTagName(baseTag);
    }
}
