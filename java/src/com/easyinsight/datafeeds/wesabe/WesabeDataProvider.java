package com.easyinsight.datafeeds.wesabe;

import com.easyinsight.users.Credentials;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.IRow;
import com.easyinsight.logging.LogClass;

import java.util.Properties;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

/**
 * User: James Boe
 * Date: Aug 16, 2008
 * Time: 8:54:01 PM
 */
public class WesabeDataProvider {
    public static DataSet refresh(Credentials credentials) {
        try {
            String client_id = "EasyInsight";  // Change this to the name of your application
            String client_version = "1.0";          // Give your application a version number
            String api_version = "1.0.0";        // Document which API version you're using

            Properties sys = System.getProperties();
            String system_name = sys.getProperty("os.name");
            String system_release = sys.getProperty("os.version");

            String user_agent = client_id + "/" + client_version +
                    " (" + system_name + " " + system_release + ")" +
                    " Wesabe-API/" + api_version;

            String credentialString = credentials.getUserName() + ":" + credentials.getPassword();
            String encoding = new sun.misc.BASE64Encoder().encode(credentialString.getBytes());

            //URL wesabe = new URL("https://www.wesabe.com/accounts.xml");
            URL wesabe = new URL("https://www.wesabe.com/transactions.csv");

            URLConnection connection = wesabe.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.setRequestProperty("User-Agent", user_agent);
            InputStream content = connection.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(content);

            int ch;
            StringBuffer buf = new StringBuffer();
            while ((ch = bis.read()) > -1) {
                buf.append((char) ch);
            }
            LogClass.debug(buf.toString());

            

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(content);
            NodeList accounts = document.getElementsByTagName("account");

            DataSet accountDataSet = new DataSet();

            for (int i = 0; i < accounts.getLength(); i++) {
                Node account = accounts.item(i);
                NodeList tags = account.getChildNodes();

                String name = "";
                String balance = "";
                String id;

                for (int j = 0; j < tags.getLength(); j++) {
                    Node tag = tags.item(j);

                    if ("name".equals(tag.getNodeName())) {
                        name = tag.getFirstChild().getNodeValue();
                    } else if ("current-balance".equals(tag.getNodeName())) {
                        balance = tag.getFirstChild().getNodeValue();
                    } else if ("id".equals(tag.getNodeName())) {
                        id = tag.getFirstChild().getNodeValue();
                    }
                }

                IRow row = accountDataSet.createRow();
                row.addValue("name", name);
                row.addValue("amount", balance);
                LogClass.debug(name + ":\t" + balance);
            }
            
            return accountDataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
