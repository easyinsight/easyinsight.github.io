package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.NumericValue;

import java.util.*;
import java.io.InputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 3:31:43 PM
 */
public class CloudWatchUtil {

    private static String getDateAsISO8601String(Date date) {
        String result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
        //convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
        //- note the added colon for the Timezone
        result = result.substring(0, result.length()-2)
        + ":" + result.substring(result.length()-2);
        return result;
    }

    public static DataSet getDataSet(String key, String secretKey, EC2Info ec2Info, AnalysisMeasure analysisMeasure, Collection<AnalysisDimension> dimensions,
                                     Date startDate, Date endDate, int period)
            throws NoSuchAlgorithmException,
            IOException, InvalidKeyException, ParserConfigurationException, SAXException, ParseException {
        Map<String, String> params = new HashMap<String, String>();
        /*params.put("Action", "ListMetrics");
        params.put("SignatureMethod", "HmacSHA256");
        params.put("SignatureVersion", "2");
        params.put("Version", "2009-05-15");*/

        params.put("Action", "GetMetricStatistics");
        params.put("StartTime", getDateAsISO8601String(startDate));
        params.put("EndTime", getDateAsISO8601String(endDate));
        if (ec2Info != null) {
            //params.put("Dimension", "InstanceId");
            params.put("Dimensions.member.1.Name", "InstanceId");
            params.put("Dimensions.member.1.Value", ec2Info.getInstanceID());
            //params.put("Dimension.member.2", "x");
            //params.put("Dimensions.member.1", "Name");
            //params.put("Dimensions.member.1", "InstanceId=" + ec2Info.getInstanceID());
            //params.put("Dimensions.member.3", "Value");
            //params.put("Dimensions.member.2", ec2Info.getInstanceID());
        }
        params.put("Namespace", "AWS/EC2");
        params.put("Period", "3600");
        //params.put("Statistics.member.1", "Average");
        if (analysisMeasure.getAggregation() == AggregationTypes.AVERAGE) {
            params.put("Statistics.member.1", "Average");
        } else if (analysisMeasure.getAggregation() == AggregationTypes.SUM) {
            params.put("Statistics.member.1", "Sum");
        } else if (analysisMeasure.getAggregation() == AggregationTypes.MAX) {
            params.put("Statistics.member.1", "Maximum");
        } else if (analysisMeasure.getAggregation() == AggregationTypes.MIN) {
            params.put("Statistics.member.1", "Minimum");
        } else if (analysisMeasure.getAggregation() == AggregationTypes.COUNT) {
            params.put("Statistics.member.1", "Samples");
        }
        params.put("MeasureName", analysisMeasure.getKey().toKeyString());
        params.put("SignatureMethod", "HmacSHA256");
        params.put("SignatureVersion", "2");
        params.put("Version", "2009-05-15");

        String url = new SignedRequestsHelper(key, secretKey).sign(params);
        System.out.println(url);
        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod(url);
        int statusCode = httpClient.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {
          System.err.println("Method failed: " + method.getStatusLine());
            System.out.println(method.getStatusText());
        }

        //System.out.println(method.getResponseBodyAsString());
        InputStream content = method.getResponseBodyAsStream();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(content);
        Node result = document.getChildNodes().item(0);
        Node datapoints = result.getChildNodes().item(1).getChildNodes().item(1);
        System.out.println(datapoints.getNodeName());
        NodeList members = datapoints.getChildNodes();
        DataSet dataSet = new DataSet();
        for (int i = 0; i < members.getLength(); i++) {
            Node memberNode = members.item(i);
            if ("member".equals(memberNode.getNodeName())) {
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
                String timestamp = memberNode.getChildNodes().item(1).getTextContent();
                Date dateVal = format.parse(timestamp);

                String value = null;
                if (analysisMeasure.getAggregation() == AggregationTypes.AVERAGE) {
                    value = findNode("Average", memberNode);
                } else if (analysisMeasure.getAggregation() == AggregationTypes.SUM) {
                    value = findNode("Sum", memberNode);
                } else if (analysisMeasure.getAggregation() == AggregationTypes.MAX) {
                    value = findNode("Maximum", memberNode);
                } else if (analysisMeasure.getAggregation() == AggregationTypes.MIN) {
                    value = findNode("Minimum", memberNode);
                } else if (analysisMeasure.getAggregation() == AggregationTypes.COUNT) {
                    value = findNode("Samples", memberNode);
                }
                if (value != null) {
                    System.out.println(dateVal + " - " + value);
                    row.addValue(CloudWatchDataSource.DATE, new DateValue(dateVal));
                    row.addValue(analysisMeasure.getKey(), new NumericValue(Double.parseDouble(value)));
                }
            }
        }
        return dataSet;
    }

    private static String findNode(String nodeName, Node node) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            if (nodeName.equals(child.getNodeName())) {
                return child.getTextContent();
            }
        }
        return null;
    }
}
