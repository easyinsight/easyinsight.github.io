package com.easyinsight.datafeeds.wesabe;

/**
 * User: James Boe
 * Date: Jun 30, 2008
 * Time: 3:48:21 PM
 */

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import com.easyinsight.logging.LogClass;

public class WesabeClient {
    public static void main(String[] args) throws Exception {
        String client_id = "EasyInsight";  // Change this to the name of your application
        String client_version = "1.0";          // Give your application a version number
        String api_version = "1.0.0";        // Document which API version you're using

        Properties sys = System.getProperties();
        String system_name = sys.getProperty("os.name");
        String system_release = sys.getProperty("os.version");

        String user_agent = client_id + "/" + client_version +
                " (" + system_name + " " + system_release + ")" +
                " Wesabe-API/" + api_version;

        if (args.length != 2) {
            System.err.println("Usage: java WesabeClient <username> <password>");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];
        String credentials = username + ":" + password;
        String encoding = new sun.misc.BASE64Encoder().encode(credentials.getBytes());

        //URL wesabe = new URL("https://www.wesabe.com/accounts.xml");
        URL wesabe = new URL("https://www.wesabe.com/transactions.xml");

        URLConnection connection = wesabe.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + encoding);
        connection.setRequestProperty("User-Agent", user_agent);
        InputStream content = connection.getInputStream();

       // BufferedInputStream bis = new BufferedInputStream(content);

        Map<Long, String> accountMap = getAccounts(username, password);


     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     DocumentBuilder builder = factory.newDocumentBuilder();
     Document document = builder.parse(content);
     NodeList transactions = document.getElementsByTagName("txaction");
        for (int i = 0; i < transactions.getLength(); i++) {
            Node transaction = transactions.item(i);
            NodeList transactionChildren = transaction.getChildNodes();
            for (int j = 0; j < transactionChildren.getLength(); j++) {
                Node transactionChildNode = transactionChildren.item(j);
                if ("account-id".equals(transactionChildNode.getNodeName())) {
                    System.out.println(transactionChildNode.getFirstChild().getNodeValue());
                } else if ("date".equals(transactionChildNode.getNodeName())) {

                } else if ("amount".equals(transactionChildNode.getNodeName())) {

                } else if ("merchant".equals(transactionChildNode.getNodeName())) {
                    
                } else if ("tags".equals(transactionChildNode.getNodeName())) {
                    
                }
            }
        }

 /*try {
     DocumentBuilder builder = factory.newDocumentBuilder();
     Document document = builder.parse(content);
     NodeList accounts = document.getElementsByTagName("account");

     for (int i = 0; i < accounts.getLength(); i++) {
         Node account  = accounts.item(i);
         NodeList tags = account.getChildNodes();

         String name = "";
         String balance = "";
         String id;

         for (int j = 0; j < tags.getLength(); j++) {
             Node tag = tags.item(j);

             if (tag.getNodeName() == "name") {
                 name = tag.getFirstChild().getNodeValue();
             } else if (tag.getNodeName() == "current-balance") {
                 balance = tag.getFirstChild().getNodeValue();
             } else if ("id".equals(tag.getNodeName())) {
                 id = tag.getFirstChild().getNodeValue();
             }
         }

         LogClass.debug(name + ":\t" + balance);
     }
 } catch (Exception e) {
     LogClass.error(e);
 } */
    }

    private static Map<Long, String> getAccounts(String username, String password) {
        Map<Long, String> accounts = new HashMap<Long, String>();
        return accounts;
    }
}
//}

