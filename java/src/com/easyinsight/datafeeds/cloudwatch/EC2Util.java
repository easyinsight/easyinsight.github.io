package com.easyinsight.datafeeds.cloudwatch;

import org.xml.sax.SAXException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.security.SignatureException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 3:26:42 PM
 */
public class EC2Util {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static String hmacString = "Action{0}AWSAccessKeyId{1}SignatureVersion1Timestamp{2}Version2006-10-01";
    private static String queryString = "https://ec2.amazonaws.com?Action={0}&AWSAccessKeyId={1}&SignatureVersion=1&Timestamp={2}&" +
            "Version=2006-10-01&Signature={3}";

    public static List<EC2Info> getInstances(String key, String secretKey) throws ParserConfigurationException, SignatureException, IOException, SAXException {
        String action = "DescribeInstances";
        
            Date date = new Date();
            String timestamp = getDateAsISO8601String(date);
            String accessKey = key;
            String signature = MessageFormat.format(hmacString, action, accessKey, timestamp);

            String base64Sig = calculateRFC2104HMAC(signature, secretKey);
            timestamp = URLEncoder.encode(getDateAsISO8601String(date), "UTF-8");
            String urlString = MessageFormat.format(queryString, action, accessKey, timestamp, URLEncoder.encode(base64Sig, "UTF-8"));

            HttpClient httpClient = new HttpClient();
            HttpMethod method = new GetMethod(urlString);
            int statusCode = httpClient.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + method.getStatusLine());
                throw new RuntimeException(method.getStatusLine().toString());
            }

            InputStream content = method.getResponseBodyAsStream();


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(content);
            List<EC2Info> instances = new ArrayList<EC2Info>();
            NodeList transactions = document.getElementsByTagName("reservationSet");
            if (transactions.getLength() == 0) {

            } else {
                Node root = transactions.item(0);
                NodeList items = root.getChildNodes();
                for (int i = 0; i < items.getLength(); i++) {
                    Node itemNode = items.item(i);
                    if ("item".equals(itemNode.getNodeName())) {
                        String groupName = "";
                        List<EC2Info> subInstances = new ArrayList<EC2Info>();
                        for (int j = 0; j < itemNode.getChildNodes().getLength(); j++) {
                            Node propertyNode = itemNode.getChildNodes().item(j);

                            if ("instancesSet".equals(propertyNode.getNodeName())) {
                                for(int k = 0;k < propertyNode.getChildNodes().getLength();k++) {
                                    Node infoNode = propertyNode.getChildNodes().item(k);
                                    if(infoNode.hasChildNodes()) {
                                        String state = infoNode.getChildNodes().item(5).getChildNodes().item(3).getFirstChild().getNodeValue();
                                        if ("running".equals(state)) {
                                            EC2Info subInfo = new EC2Info();
                                            String instanceID = infoNode.getChildNodes().item(1).getFirstChild().getNodeValue();

                                            subInfo.setInstanceID(instanceID);
                                            String amiID = infoNode.getChildNodes().item(3).getFirstChild().getNodeValue();
                                            subInfo.setAmiID(amiID);


                                            String dns = infoNode.getChildNodes().item(7).getFirstChild().getNodeValue();
                                            subInfo.setHostName(dns);

                                            //instances.add(dns);
                                            subInstances.add(subInfo);
                                        }
                                    }
                                }
                            } else if ("groupSet".equals(propertyNode.getNodeName())) {
                                NodeList groupNodes = propertyNode.getChildNodes().item(1).getChildNodes();
                                StringBuilder groupBuilder = new StringBuilder();
                                for (int k = 0; k < groupNodes.getLength(); k++) {
                                    Node groupNode = groupNodes.item(k);
                                    if ("groupId".equals(groupNode.getNodeName())) {
                                        String group = groupNode.getTextContent();
                                        groupBuilder.append(group).append(",");
                                        
                                    }
                                }

                                if (groupBuilder.length() > 0) {
                                   groupName = (groupBuilder.substring(0, groupBuilder.length() - 1));
                                }
                            }
                        }
                        for (EC2Info info : subInstances) {
                            info.setGroup(groupName);
                            instances.add(info);
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

    public static DataSet createDataSet(List<EC2Info> ec2Infos, Collection<AnalysisDimension> dimensions) {
        DataSet dataSet = new DataSet();
        for (EC2Info ec2Info : ec2Infos) {
            IRow row = dataSet.createRow();
            for (AnalysisDimension dimension : dimensions) {
                if (CloudWatchDataSource.IMAGE_ID.equals(dimension.getKey().toKeyString())) {
                    row.addValue(dimension.getKey(), ec2Info.getAmiID());
                } else if (CloudWatchDataSource.INSTANCE_ID.equals(dimension.getKey().toKeyString())) {
                    row.addValue(dimension.getKey(), ec2Info.getInstanceID());
                } else if (CloudWatchDataSource.GROUP_NAME.equals(dimension.getKey().toKeyString())) {
                    row.addValue(dimension.getKey(), ec2Info.getGroup());
                } else if (CloudWatchDataSource.HOST_NAME.equals(dimension.getKey().toKeyString())) {
                    row.addValue(dimension.getKey(), ec2Info.getHostName());
                }
            }
        }
        return dataSet;
    }

    public static void main(String[] args) throws IOException, SignatureException, ParserConfigurationException, SAXException {
        EC2Util.getInstances("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
    }
}
