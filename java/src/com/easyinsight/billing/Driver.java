/*  This software code is made available "AS IS" without warranties of any 
	kind.  You may copy, display, modify and redistribute the software
	code either by itself or as incorporated into your code; provided that
	you do not remove any proprietary notices.  Your use of this software
	code is at your own risk and you waive any claim against Amazon
	Web Services LLC or its affiliates with respect to your use of this 
	software code. (c) Amazon Web Services LLC or its affiliates.*/

package com.easyinsight.billing;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.easyinsight.logging.LogClass;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class Driver {

    private String httpQuery;
    private boolean pay;

    private String doHttpQuery(URL endPoint) throws Exception {
		HttpURLConnection con = (HttpURLConnection) endPoint.openConnection();
		LogClass.debug("\n\nRequest Query string:\n");
		LogClass.debug(endPoint.toString());
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.connect();
		String respStr;

        String tokenID = "";

        if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			formatString(con.getErrorStream());
			throw new Exception("\n\nReceived response code "
					+ con.getResponseCode() + " from server");
		} else {
			respStr = getResponseString(con.getInputStream());
			LogClass.debug("\n\nResponse:\n");
			//tokenID = formatString(new StringInputStream(respStr));
		}
		con.disconnect();
        return tokenID;
    }

    private String doPayQuery(URL endPoint) throws Exception {
		HttpURLConnection con = (HttpURLConnection) endPoint.openConnection();
		LogClass.debug("\n\nRequest Query string:\n");
		LogClass.debug(endPoint.toString());
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.connect();
		String respStr;

        String tokenID = "";

        if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			payString(con.getErrorStream());
			throw new Exception("\n\nReceived response code "
					+ con.getResponseCode() + " from server");
		} else {
			respStr = getResponseString(con.getInputStream());
			LogClass.debug("\n\nResponse:\n");
			//tokenID = payString(new StringInputStream(respStr));
		}
		con.disconnect();
        return tokenID;
    }

    private String fpsCaller(String httpQuery) throws Exception {
        URL endPoint = new URL(Config.salUrl+"?"+httpQuery);
        if (pay) {
            return doPayQuery(endPoint);
        } else {
		    return doHttpQuery(endPoint);
        }
    }

    public Driver(String httpQuery) {
        this.httpQuery = httpQuery;
    }

    public Driver(String httpQuery, boolean pay) {
        this(httpQuery);
        this.pay = pay;
    }

    public String call() throws Exception {
        return this.fpsCaller(httpQuery);
    }

    private String getResponseString(InputStream queryResp) throws Exception {
		final InputStreamReader inputStreamReader = new InputStreamReader(
				queryResp);
		BufferedReader buffReader = new BufferedReader(inputStreamReader);

		String line;
		StringBuffer stringBuff = new StringBuffer();

		while ((line = buffReader.readLine()) != null) {
			stringBuff.append(line);
		}

		return stringBuff.toString();
	}

    private String formatString(InputStream is) throws Exception {
		OutputFormat format = new OutputFormat();
		format.setLineWidth(65);
		format.setIndenting(true);
		format.setIndent(2);

		ByteArrayOutputStream w = new ByteArrayOutputStream();
		XMLSerializer serializer = new XMLSerializer(w, format);

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        NodeList elements = document.getElementsByTagName("TokenId");
        Node node = elements.item(0);
        String tokenString = node.getFirstChild().getNodeValue();
        LogClass.debug("token string = " + tokenString);
        serializer.serialize(document);
		String SOAPMsg = w.toString();

		LogClass.debug("\n" + SOAPMsg);

        return tokenString;
    }

    private String payString(InputStream is) throws Exception {
		OutputFormat format = new OutputFormat();
		format.setLineWidth(65);
		format.setIndenting(true);
		format.setIndent(2);

		ByteArrayOutputStream w = new ByteArrayOutputStream();
		XMLSerializer serializer = new XMLSerializer(w, format);

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        /*NodeList elements = document.getElementsByTagName("TokenId");
        Node node = elements.item(0);
        String tokenString = node.getFirstChild().getNodeValue();
        LogClass.debug("token string = " + tokenString);*/
        serializer.serialize(document);
		String SOAPMsg = w.toString();

		LogClass.debug("\n" + SOAPMsg);

        return null;
    }
	
}
