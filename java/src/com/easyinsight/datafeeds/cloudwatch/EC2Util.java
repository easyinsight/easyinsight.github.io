package com.easyinsight.datafeeds.cloudwatch;

import com.xerox.amazonws.ec2.VolumeInfo;
import nu.xom.Builder;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
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
import java.io.ByteArrayInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
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
    private static String dbHmacString = "Action{0}AWSAccessKeyId{1}SignatureVersion1Timestamp{2}Version2010-07-28";
    private static String queryString = "https://ec2.amazonaws.com?Action={0}&AWSAccessKeyId={1}&SignatureVersion=1&Timestamp={2}&" +
            "Version=2006-10-01&Signature={3}";

    private static String dbQueryString = "https://rds.amazonaws.com/?Action={0}&AWSAccessKeyId={1}&SignatureVersion=2&SignatureMethod=HmacSHA256&Timestamp={2}&" +
            "Version=2010-07-28&Signature={3}";

    public static List<String> getDatabaseInstances(String key, String secretKey) throws ParserConfigurationException, SignatureException, IOException, SAXException, InvalidKeyException, NoSuchAlgorithmException, ParsingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Action", "DescribeDBInstances");
        params.put("SignatureMethod", "HmacSHA256");
        params.put("SignatureVersion", "2");
        params.put("Version", "2010-07-28");
        String url = new RDSSignedRequestsHelper(key, secretKey).sign(params);

        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod(url);
        int statusCode = httpClient.executeMethod(method);
        String string = method.getResponseBodyAsString();
        string = string.replace("xmlns=\"http://rds.amazonaws.com/doc/2010-07-28/\"", "");
        nu.xom.Document doc = new Builder().build(new ByteArrayInputStream(string.getBytes()));
        Nodes instanceNodes = doc.query("/DescribeDBInstancesResponse/DescribeDBInstancesResult/DBInstances/DBInstance");
        List<String> instances = new ArrayList<String>();
        for (int i = 0; i < instanceNodes.size(); i++) {
            nu.xom.Node node = instanceNodes.get(i);
            instances.add(node.query("DBInstanceIdentifier/text()").get(0).getValue());
        }

        return instances;
    }

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

    public static DataSet createDataSetForEBS(List<VolumeInfo> volumeInfos, Collection<AnalysisDimension> dimensions) {
        DataSet dataSet = new DataSet();
        for (VolumeInfo volumeInfo : volumeInfos) {
            IRow row = dataSet.createRow();
            for (AnalysisDimension dimension : dimensions) {
                if (AmazonEBSSource.VOLUME.equals(dimension.getKey().toKeyString())) {
                    row.addValue(dimension.createAggregateKey(), volumeInfo.getVolumeId());
                }
            }
        }
        return dataSet;
    }

    public static DataSet createDataSetForRDS(List<String> infos, Collection<AnalysisDimension> dimensions) {
        DataSet dataSet = new DataSet();
        for (String info : infos) {
            IRow row = dataSet.createRow();
            for (AnalysisDimension dimension : dimensions) {
                if (AmazonRDSSource.INSTANCE_IDENTIFIER.equals(dimension.getKey().toKeyString())) {
                    row.addValue(dimension.createAggregateKey(), info);
                }
            }
        }
        return dataSet;
    }

    public static DataSet createDataSet(List<EC2Info> ec2Infos, Collection<AnalysisDimension> dimensions) {
        DataSet dataSet = new DataSet();
        for (EC2Info ec2Info : ec2Infos) {
            IRow row = dataSet.createRow();
            for (AnalysisDimension dimension : dimensions) {
                if (AmazonEC2Source.IMAGE_ID.equals(dimension.getKey().toKeyString())) {
                    row.addValue(dimension.createAggregateKey(), ec2Info.getAmiID());
                } else if (AmazonEC2Source.INSTANCE_ID.equals(dimension.getKey().toKeyString())) {
                    row.addValue(dimension.createAggregateKey(), ec2Info.getInstanceID());
                } else if (AmazonEC2Source.GROUP_NAME.equals(dimension.getKey().toKeyString())) {
                    row.addValue(dimension.createAggregateKey(), ec2Info.getGroup());
                } else if (AmazonEC2Source.HOST_NAME.equals(dimension.getKey().toKeyString())) {
                    row.addValue(dimension.createAggregateKey(), ec2Info.getHostName());
                }
            }
        }
        return dataSet;
    }

    public static void main(String[] args) throws IOException, SignatureException, ParserConfigurationException, SAXException, InvalidKeyException, NoSuchAlgorithmException, ParsingException {
        EC2Util.getDatabaseInstances("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
    }
}
