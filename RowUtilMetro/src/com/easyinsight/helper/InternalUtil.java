package com.easyinsight.helper;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;

/**
 * User: jamesboe
 * Date: 1/6/11
 * Time: 11:22 AM
 */
public class InternalUtil {
    static Document sendXML(String xml, String key, String secretKey, String call) throws IOException, SAXException, ParserConfigurationException {
        URL url = new URL("https://www.easy-insight.com/app/xml/" + call);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        BASE64Encoder enc = new sun.misc.BASE64Encoder();
        String userpassword = key + ":" + secretKey;
        String encodedAuthorization = enc.encode( userpassword.getBytes() );
        connection.setRequestProperty("Authorization", "Basic "+
            encodedAuthorization);
        OutputStreamWriter out = new OutputStreamWriter(
                                      connection.getOutputStream());
        out.write(xml);
        out.close();

        BufferedReader in = null;
        try {
            in = new BufferedReader(
                        new InputStreamReader(
                        connection.getInputStream()));
        } catch (IOException e) {
            in = new BufferedReader(
                        new InputStreamReader(
                        connection.getErrorStream()));
        }

        StringBuilder result = new StringBuilder();

        String line;

        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        in.close();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new ByteArrayInputStream(result.toString().getBytes()));
        document.getDocumentElement().normalize();
        return document;
    }
}
