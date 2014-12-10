package com.easyinsight.watchdog.updatetask;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.codec.binary.Base64;
import org.apache.tools.ant.BuildException;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;
import java.io.InputStream;
import java.io.IOException;
import java.security.SignatureException;

/**
 * User: James Boe
 * Date: Jan 27, 2009
 * Time: 3:32:38 PM
 */
public class AppInstanceTask extends Task {


    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private String password;
    private String userName;
    private String role;
    private String operation;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public void execute() throws BuildException {
        try {
            HttpClient httpClient = new HttpClient();
            httpClient.getParams().setAuthenticationPreemptive(true);
            Credentials defaultcreds = new UsernamePasswordCredentials(getUserName(), getPassword());
            httpClient.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            for (Instance instance : getInstances()) {
                HttpMethod updateMethod = new GetMethod("http://" + instance.host + ":4000/?operation=" + getOperation() + "&type=" + URLEncoder.encode(instance.type, "UTF-8"));
                httpClient.executeMethod(updateMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }

    public class Instance {
        public String host;
        public String type;
    }

    protected List<Instance> getInstances() throws ParserConfigurationException, SignatureException, IOException, SAXException {
        String action = "DescribeInstances";
        Date date = new Date();
        String timestamp = getDateAsISO8601String(date);
        String accessKey = "0AWCBQ78TJR8QCY8ABG2";
        String hmacString = "Action{0}AWSAccessKeyId{1}SignatureVersion1Timestamp{2}Version2014-10-01";
        String signature = MessageFormat.format(hmacString, action, accessKey, timestamp);
        System.out.println(signature);
        String base64Sig = calculateRFC2104HMAC(signature, "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
        timestamp = URLEncoder.encode(getDateAsISO8601String(date), "UTF-8");
        String queryString = "https://ec2.amazonaws.com?Action={0}&AWSAccessKeyId={1}&SignatureVersion=1&Timestamp={2}&" +
                "Version=2014-10-01&Signature={3}";
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

        try {
            printDocument(document, System.out);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        List<Instance> instances = new ArrayList<>();
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
                            for (int k = 0; k < propertyNode.getChildNodes().getLength(); k++) {
                                Node infoNode = propertyNode.getChildNodes().item(k);
                                if (infoNode.hasChildNodes()) {
                                    boolean correctRole = false;
                                    String type = "";
                                    for (int l = 0; l < infoNode.getChildNodes().getLength(); l++) {
                                        if ("tagSet".equals(infoNode.getChildNodes().item(l).getNodeName())) {
                                            Node tagsNode = infoNode.getChildNodes().item(l);
                                            for (int m = 0; m < tagsNode.getChildNodes().getLength(); m++) {
                                                Node item = tagsNode.getChildNodes().item(m);
                                                String key = null;
                                                String value = null;
                                                for (int n = 0; n < item.getChildNodes().getLength(); n++) {
                                                    if ("key".equals(item.getChildNodes().item(n).getNodeName()))
                                                        key = item.getChildNodes().item(n).getTextContent();
                                                    else if ("value".equals(item.getChildNodes().item(n).getNodeName()))
                                                        value = item.getChildNodes().item(n).getTextContent();
                                                }
                                                if (getRole() == null || "Role".equals(key) && getRole().equals(value)) {
                                                    correctRole = true;
                                                }
                                            }
                                        }
                                        if("instanceType".equals(infoNode.getChildNodes().item(l).getNodeName())) {
                                            type = infoNode.getChildNodes().item(l).getTextContent();
                                        }
                                    }
                                    String state = infoNode.getChildNodes().item(5).getChildNodes().item(3).getFirstChild().getNodeValue();
                                    if (correctRole && "running".equals(state)) {
                                        Instance curInstance = new Instance();
                                        String dns = infoNode.getChildNodes().item(7).getFirstChild().getNodeValue();
                                        System.out.println(dns);
                                        curInstance.host = dns;
                                        curInstance.type = type;
                                        System.out.println(type);
                                        instances.add(curInstance);
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

    private static String getDateAsISO8601String(Date date) {
        String result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
        //convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
        //- note the added colon for the Timezone
        result = result.substring(0, result.length() - 2)
                + ":" + result.substring(result.length() - 2);
        return result;
    }

    private static String calculateRFC2104HMAC(String data, String key)
            throws java.security.SignatureException {
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
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(doc),
             new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }
}
