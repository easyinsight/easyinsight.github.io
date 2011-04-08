package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import com.xerox.amazonws.ec2.VolumeInfo;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 3:31:43 PM
 */
public class CloudWatchUtil {

    private static String getDateAsISO8601String(Date date) {
        String result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
        result = result.substring(0, result.length()-2)
        + ":" + result.substring(result.length()-2);
        return result;
    }

    public static DataSet getEBSDataSet(String key, String secretKey, VolumeInfo volumeInfo, AnalysisMeasure analysisMeasure, Collection<AnalysisDimension> dimensions,
                                     Date startDate, Date endDate, int period, AnalysisDateDimension dateDimension) throws Exception {
        Map<String, String> params = new HashMap<String, String>();

        params.put("Action", "GetMetricStatistics");
        params.put("StartTime", getDateAsISO8601String(startDate));
        params.put("EndTime", getDateAsISO8601String(endDate));
        if (volumeInfo != null) {
            params.put("Dimensions.member.1.Name", "VolumeId");
            params.put("Dimensions.member.1.Value", volumeInfo.getVolumeId());
        }
        params.put("Namespace", "AWS/EBS");
        params.put("Period", String.valueOf(period));
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
        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod(url);
        int statusCode = httpClient.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {
          System.err.println("Method failed: " + method.getStatusLine());
            System.out.println(method.getStatusText());
        }

        String string = method.getResponseBodyAsString();
        string = string.replace("xmlns=\"http://monitoring.amazonaws.com/doc/2009-05-15/\"", "");
        Builder builder = new Builder();
        Document doc = builder.build(new ByteArrayInputStream(string.getBytes()));
        DataSet dataSet = new DataSet();
        Nodes dataPointNodes = doc.query("/GetMetricStatisticsResponse/GetMetricStatisticsResult/Datapoints/member");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        for (int i = 0; i < dataPointNodes.size(); i++) {
            IRow row = dataSet.createRow();
            Node dataPointNode = dataPointNodes.get(i);
            String timestampString = dataPointNode.query("Timestamp/text()").get(0).getValue();
            Date time = format.parse(timestampString);
            row.addValue(dateDimension.createAggregateKey(), time);
            String value = "0";
            if (analysisMeasure.getAggregation() == AggregationTypes.AVERAGE) {
                value = dataPointNode.query("Average/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.SUM) {
                value = dataPointNode.query("Sum/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.MAX) {
                value = dataPointNode.query("Maximum/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.MIN) {
                value = dataPointNode.query("Minimum/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.COUNT) {
                value = dataPointNode.query("Samples/text()").get(0).getValue();
            }
            double result = Double.parseDouble(value);
            row.addValue(analysisMeasure.createAggregateKey(), result);
            if (volumeInfo != null) {
                for (AnalysisDimension dimension : dimensions) {
                    if (AmazonEBSSource.VOLUME.equals(dimension.getKey().toKeyString())) {
                        row.addValue(dimension.createAggregateKey(), volumeInfo.getVolumeId());
                    }
                }
            }
        }
        return dataSet;
    }

    public static DataSet getRDSDataSet(String key, String secretKey, String dbInstance, AnalysisMeasure analysisMeasure, Collection<AnalysisDimension> dimensions,
                                     Date startDate, Date endDate, int period, AnalysisDateDimension dateDimension) throws Exception {
        Map<String, String> params = new HashMap<String, String>();

        params.put("Action", "GetMetricStatistics");
        params.put("StartTime", getDateAsISO8601String(startDate));
        params.put("EndTime", getDateAsISO8601String(endDate));
        if (dbInstance != null) {
            params.put("Dimensions.member.1.Name", "DBInstanceIdentifier");
            params.put("Dimensions.member.1.Value", dbInstance);
        }
        params.put("Namespace", "AWS/RDS");
        params.put("Period", String.valueOf(period));
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
        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod(url);
        int statusCode = httpClient.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {
          System.err.println("Method failed: " + method.getStatusLine());
            System.out.println(method.getStatusText());
        }

        String string = method.getResponseBodyAsString();
        string = string.replace("xmlns=\"http://monitoring.amazonaws.com/doc/2009-05-15/\"", "");
        Builder builder = new Builder();
        Document doc = builder.build(new ByteArrayInputStream(string.getBytes()));
        DataSet dataSet = new DataSet();
        Nodes dataPointNodes = doc.query("/GetMetricStatisticsResponse/GetMetricStatisticsResult/Datapoints/member");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        for (int i = 0; i < dataPointNodes.size(); i++) {
            IRow row = dataSet.createRow();
            Node dataPointNode = dataPointNodes.get(i);
            String timestampString = dataPointNode.query("Timestamp/text()").get(0).getValue();
            Date time = format.parse(timestampString);
            row.addValue(dateDimension.createAggregateKey(), time);
            String value = "0";
            if (analysisMeasure.getAggregation() == AggregationTypes.AVERAGE) {
                value = dataPointNode.query("Average/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.SUM) {
                value = dataPointNode.query("Sum/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.MAX) {
                value = dataPointNode.query("Maximum/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.MIN) {
                value = dataPointNode.query("Minimum/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.COUNT) {
                value = dataPointNode.query("Samples/text()").get(0).getValue();
            }
            double result = Double.parseDouble(value);
            row.addValue(analysisMeasure.createAggregateKey(), result);
            if (dbInstance != null) {
                for (AnalysisDimension dimension : dimensions) {
                    if (AmazonRDSSource.INSTANCE_IDENTIFIER.equals(dimension.getKey().toKeyString())) {
                        row.addValue(dimension.createAggregateKey(), dbInstance);
                    }
                }
            }
        }
        return dataSet;
    }

    public static DataSet getDataSet(String key, String secretKey, EC2Info ec2Info, AnalysisMeasure analysisMeasure, Collection<AnalysisDimension> dimensions,
                                     Date startDate, Date endDate, int period, AnalysisDateDimension dateDimension)
            throws NoSuchAlgorithmException,
            IOException, InvalidKeyException, ParserConfigurationException, SAXException, ParseException, ParsingException {
        Map<String, String> params = new HashMap<String, String>();

        params.put("Action", "GetMetricStatistics");
        params.put("StartTime", getDateAsISO8601String(startDate));
        params.put("EndTime", getDateAsISO8601String(endDate));
        if (ec2Info != null) {
            params.put("Dimensions.member.1.Name", "InstanceId");
            params.put("Dimensions.member.1.Value", ec2Info.getInstanceID());
        }
        params.put("Namespace", "AWS/EC2");
        params.put("Period", String.valueOf(period));
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
        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod(url);
        int statusCode = httpClient.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {
          System.err.println("Method failed: " + method.getStatusLine());
            System.out.println(method.getStatusText());
        }

        String string = method.getResponseBodyAsString();
        string = string.replace("xmlns=\"http://monitoring.amazonaws.com/doc/2009-05-15/\"", "");
        Builder builder = new Builder();
        Document doc = builder.build(new ByteArrayInputStream(string.getBytes()));
        DataSet dataSet = new DataSet();
        Nodes dataPointNodes = doc.query("/GetMetricStatisticsResponse/GetMetricStatisticsResult/Datapoints/member");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        for (int i = 0; i < dataPointNodes.size(); i++) {
            IRow row = dataSet.createRow();
            Node dataPointNode = dataPointNodes.get(i);
            String timestampString = dataPointNode.query("Timestamp/text()").get(0).getValue();
            Date time = format.parse(timestampString);
            row.addValue(dateDimension.createAggregateKey(), time);
            String value = "0";
            if (analysisMeasure.getAggregation() == AggregationTypes.AVERAGE) {
                value = dataPointNode.query("Average/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.SUM) {
                value = dataPointNode.query("Sum/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.MAX) {
                value = dataPointNode.query("Maximum/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.MIN) {
                value = dataPointNode.query("Minimum/text()").get(0).getValue();
            } else if (analysisMeasure.getAggregation() == AggregationTypes.COUNT) {
                value = dataPointNode.query("Samples/text()").get(0).getValue();
            }
            double result = Double.parseDouble(value);
            row.addValue(analysisMeasure.createAggregateKey(), result);
            if (ec2Info != null) {
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
        }
        return dataSet;
    }

    public static Document blah(String key, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException, IOException, ParsingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Action", "ListMetrics");
        params.put("Namespace", "AWS/RDS");
        params.put("SignatureMethod", "HmacSHA256");
        params.put("SignatureVersion", "2");
        params.put("Version", "2009-05-15");
        String url = new SignedRequestsHelper(key, secretKey).sign(params);
        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod(url);
        int statusCode = httpClient.executeMethod(method);
        String string = method.getResponseBodyAsString();
        string = string.replaceAll("xmlns=\"http://monitoring.amazonaws.com/doc/2009-05-15/\"", "");
        return new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));
    }

    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParsingException {
        Document doc = blah("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        Nodes members = doc.query("/ListMetricsResponse/ListMetricsResult/Metrics/member");
        for (int i = 0; i < members.size(); i++) {
            Node member = members.get(i);
            String measureName = member.query("MeasureName/text()").get(0).getValue();
            String nameSpace = member.query("Namespace/text()").get(0).getValue();
            Nodes dimensions = member.query("Dimensions/member/Name/text()");
            if (dimensions.size() > 0) {
                System.out.println("Measure = " + measureName);
                System.out.println("Namespace = " + nameSpace);
                String dimensionName = member.query("Dimensions/member/Name/text()").get(0).getValue();
                String dimensionValue = member.query("Dimensions/member/Value/text()").get(0).getValue();
                System.out.println("\t" + dimensionName + " = " + dimensionValue);
                Set<String> strings = map.get(nameSpace);
                if (strings == null) {
                    strings = new HashSet<String>();
                    map.put(nameSpace, strings);
                }
                strings.add(measureName);
            }
        }
        System.out.println(map);
    }
}
