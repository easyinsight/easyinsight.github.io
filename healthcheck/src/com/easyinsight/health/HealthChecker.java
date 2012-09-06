package com.easyinsight.health;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/29/11
 * Time: 10:47 PM
 */
public class HealthChecker extends TimerTask {

    private static HealthChecker instance;

    public static void initialize() {
        instance = new HealthChecker();
    }

    public static HealthChecker instance() {
        return instance;
    }

    private int failures = 0;
    private boolean active = true;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        failures = 0;
    }

    public int getFailures() {
        return failures;
    }

    public static final int FAILURE_THRESHOLD = 2;

    public static final int RESET_THRESHOLD = 10;

    @Override
    public void run() {
        if (active) {
            try {

                boolean allGood = determineStatus();
                System.out.println("Status was " + allGood);
                if (!allGood) {
                    failures++;
                    if (failures == FAILURE_THRESHOLD) {
                        alert();
                    } else if (failures == RESET_THRESHOLD) {
                        failures = 0;
                    }
                } else {
                    failures = 0;
                }
                //if (!"Success".equals(status)) {
                    /**/
                //}

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean determineStatus() throws IOException, ParserConfigurationException, SAXException {
        try {
            URL url = new URL("https://www.easy-insight.com/app/admin/health");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(15*1000);
            connection.connect();

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
            NodeList children = document.getChildNodes().item(0).getChildNodes();
            List<Map<String, String>> results = new ArrayList<Map<String, String>>();
            for (int i = 0; i < children.getLength(); i++) {
            Node server = children.item(i);
            if (server instanceof Element) {
                Map<String, String> properties = new HashMap<String, String>();
                results.add(properties);
                NodeList serverChildren = server.getChildNodes();
                for (int j = 0; j < serverChildren.getLength(); j++) {
                    Node serverChild = serverChildren.item(j);
                    if (serverChild instanceof Element) {
                        properties.put(serverChild.getNodeName(), serverChild.getFirstChild().getNodeValue());
                    }
                }
            }

        }
            System.out.println(results);
            boolean allGood = true;
            for (Map<String, String> map : results) {
            String status = map.get("status");
            if (!"Success".equals(status)) {
                allGood = false;
            }
        }
            System.out.println("Current all good = " + allGood);
            return allGood;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void alert() throws Exception {
        TwilioRestClient client = new TwilioRestClient("ACef90386a431fd57dcc62d0b2bb8b00df", "f52b9bc3715f5896e91b0bd5af0ffd02", null);
        Map<String,String> params = new HashMap<String,String>();
        params.put("From", "7202208085");
        params.put("To", "7202858652");
        params.put("Url", "http://www.easy-insight.com/twilio.xml");
        TwilioRestResponse response = client.request("/"+"2010-04-01"+"/Accounts/"+client.getAccountSid()+"/Calls", "POST", params);

        if(response.isError())
            System.out.println("Error making outgoing call: "+response.getHttpStatus()+"\n"+response.getResponseText());
        else {
            System.out.println(response.getResponseText());

        }
    }
}
