package com.easyinsight.health;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import sun.misc.BASE64Encoder;
import javax.mail.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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

    private static int count = 0;
    private static String[] numbers = {"7202858652", "3035850830"};

    @Override
    public void run() {
        if (active) {
            System.out.println("active");
            Map<String, Long> fullValues = new HashMap<String, Long>();
            try {
                try {
                    Map<String, Long> allGood = determineStatus();
                    Set<String> deleteKeys = new HashSet<String>();
                    for (String val : fullValues.keySet()) {
                        if (!allGood.containsKey(val)) {
                            deleteKeys.add(val);
                        }
                    }
                    for (String val : deleteKeys) {
                        fullValues.remove(val);
                    }
                    for (Map.Entry<String, Long> entry : allGood.entrySet()) {
                        if (!fullValues.containsKey(entry.getKey())) {
                            fullValues.put(entry.getKey(), System.currentTimeMillis());
                        }
                        System.out.println(entry.getKey() + " is going down, last value is " + fullValues.get(entry.getKey()));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                boolean alert = false;
                for(Map.Entry<String, Long> entry : fullValues.entrySet()) {
                    if(!alert && entry.getValue() < System.currentTimeMillis() - (15 * 60 * 1000)) {
                        alert();
                        alert = true;
                    } else if(entry.getValue() < System.currentTimeMillis() - (5 * 60 * 1000)) {
                        sendEmail("jboe@easy-insight.com", "SHITS BROKE", entry.getValue() + " is down.");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, Long> determineStatus() throws Exception {
            URL url = new URL("https://www.easy-insight.com/app/admin/health.json");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(15 * 1000);
            connection.connect();
            InputStream is = connection.getInputStream();

            StringBuilder result = new StringBuilder();

            String line;
            JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            String jsonString = writer.toString();
            Object o = parser.parse(jsonString);
            Map<String, Long> statuses = new HashMap<String, Long>();
            JSONArray arr = (JSONArray) o;
            for(Object arrVal : arr) {
                JSONObject server = (JSONObject) arrVal;
                String serverIp = String.valueOf(server.get("host"));
                Long l = Long.parseLong(String.valueOf(server.get("time")));


                if(!"Success".equals(String.valueOf(server.get("code")))) {
                    statuses.put(serverIp, l);
                }
            }

            return statuses;
    }

    public static void alert() throws Exception {
        TwilioRestClient client = new TwilioRestClient("ACef90386a431fd57dcc62d0b2bb8b00df", "f52b9bc3715f5896e91b0bd5af0ffd02", null);
        Map<String, String> params = new HashMap<String, String>();
        params.put("From", "7202208085");
        params.put("To", numbers[(count++) % numbers.length]);
        params.put("Url", "http://www.easy-insight.com/twilio.xml");
        TwilioRestResponse response = client.request("/" + "2010-04-01" + "/Accounts/" + client.getAccountSid() + "/Calls", "POST", params);

        if (response.isError())
            System.out.println("Error making outgoing call: " + response.getHttpStatus() + "\n" + response.getResponseText());
        else {
            System.out.println(response.getResponseText());

        }
    }

    private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";

    private void sendEmail(String emailAddress, String subject, String htmlBody) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("vendor", "SendGrid");

        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getInstance(props, auth);
        // uncomment for debugging infos to stdout
        // mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);

        Multipart multipart = new MimeMultipart();

        message.setSubject(subject);

        BodyPart part2 = new MimeBodyPart();
        part2.setContent(htmlBody, "text/html");

        //multipart.addBodyPart(part1);
        multipart.addBodyPart(part2);

        message.setContent(multipart);

        message.setFrom(new InternetAddress("sales@easy-insight.com", "Easy Insight"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));

        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    private static final String SMTP_AUTH_USER = "jboe@easy-insight.com";
    private static final String SMTP_AUTH_PWD  = "e@symone$";

    private static class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }

}
