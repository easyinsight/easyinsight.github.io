package com.easyinsight.watchdog.updatetask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.codec.binary.Base64;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.security.SignatureException;

/**
 * User: James Boe
 * Date: Jan 27, 2009
 * Time: 3:32:38 PM
 */
public class AppInstanceTask extends Task {

    protected static final String APP_AMI = "ami-7aad4b13";
    protected static final String STAGING_AMI = "ami-0f4aab66";

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static String hmacString = "Action{0}AWSAccessKeyId{1}SignatureVersion1Timestamp{2}Version2006-10-01";
    private static String queryString = "https://ec2.amazonaws.com?Action={0}&AWSAccessKeyId={1}&SignatureVersion=1&Timestamp={2}&" +
            "Version=2006-10-01&Signature={3}";

    private String password;
    private String userName;

    protected String getAMI() {
        return APP_AMI;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    protected List<String> getInstances() throws ParserConfigurationException, SignatureException, IOException, SAXException {
        String action = "DescribeInstances";

            Date date = new Date();
            String timestamp = getDateAsISO8601String(date);
            String accessKey = "0AWCBQ78TJR8QCY8ABG2";
            String signature = MessageFormat.format(hmacString, action, accessKey, timestamp);
            System.out.println(signature);
            String base64Sig = calculateRFC2104HMAC(signature, "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            timestamp = URLEncoder.encode(getDateAsISO8601String(date), "UTF-8");
            String urlString = MessageFormat.format(queryString, action, accessKey, timestamp, URLEncoder.encode(base64Sig, "UTF-8"));
            System.out.println(urlString);
            HttpClient httpClient = new HttpClient();
            HttpMethod method = new GetMethod(urlString);
            int statusCode = httpClient.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + method.getStatusLine());
            }

            InputStream content = method.getResponseBodyAsStream();


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(content);
            List<String> instances = new ArrayList<String>();
            NodeList transactions = document.getElementsByTagName("reservationSet");
            if (transactions.getLength() == 0) {
                System.out.println("No running transactions");
            } else {
                Node root = transactions.item(0);
                NodeList items = root.getChildNodes();
                for (int i = 0; i < items.getLength(); i++) {
                    Node itemNode = items.item(i);
                    if ("item".equals(itemNode.getNodeName())) {
                        for (int j = 0; j < itemNode.getChildNodes().getLength(); j++) {
                            Node propertyNode = itemNode.getChildNodes().item(j);
                            if ("instancesSet".equals(propertyNode.getNodeName())) {
                                for(int k = 0;k < propertyNode.getChildNodes().getLength();k++) {
                                    Node infoNode = propertyNode.getChildNodes().item(k);
                                    if(infoNode.hasChildNodes()) {
                                        String state = infoNode.getChildNodes().item(5).getChildNodes().item(3).getFirstChild().getNodeValue();
                                        if ("running".equals(state)) {
                                            String amiID = infoNode.getChildNodes().item(3).getFirstChild().getNodeValue();
                                            System.out.println(amiID);
                                            if (getAMI().equals(amiID)) {
                                                String dns = infoNode.getChildNodes().item(7).getFirstChild().getNodeValue();
                                                System.out.println(dns);
                                                instances.add(dns);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        return instances;
    }

    private static String getDateAsISO8601String(Date date)
{
  String result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
  //convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
  //- note the added colon for the Timezone
  result = result.substring(0, result.length()-2)
    + ":" + result.substring(result.length()-2);
  return result;
}

    private static String calculateRFC2104HMAC(String data, String key)
            throws java.security.SignatureException
        {
            String result;
            try {
                // get an hmac_sha1 key from the raw key bytes
                SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
                                                             HMAC_SHA1_ALGORITHM);

                // get an hmac_sha1 Mac instance and initialize with the signing key
                Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
                mac.init(signingKey);

                // compute the hmac on input data bytes
                byte[] rawHmac = mac.doFinal(data.getBytes());

                // base64-encode the hmac

                result = new String(Base64.encodeBase64(rawHmac));
            }
            catch (Exception e) {
                throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
            }
            return result;
        }
}
