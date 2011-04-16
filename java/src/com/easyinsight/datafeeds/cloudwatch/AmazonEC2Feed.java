package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.dataset.DataSet;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 4/4/11
 * Time: 9:51 PM
 */
public class AmazonEC2Feed extends AmazonBaseFeed {
    protected DataSet retrieve(Collection<AnalysisDimension> dimensions, Collection<AnalysisMeasure> measures, Date startDate, Date endDate, int period, AnalysisDateDimension analysisDateDimension, String key, String secretKey) throws Exception {
        DataSet dataSet = new DataSet();
        if (measures.size() > 0) {
            if (dimensions.size() > 0) {
                // retrieve via instance IDs and aggregate
                List<EC2Info> ec2Infos = EC2Util.getInstances(key, secretKey);
                for (EC2Info info : ec2Infos) {
                    for (AnalysisMeasure analysisMeasure : measures) {
                        DataSet childSet = CloudWatchUtil.getDataSet(key, secretKey, info, analysisMeasure, dimensions, startDate, endDate,
                                period, analysisDateDimension);
                        for (IRow row : childSet.getRows()) {
                            dataSet.addRow(row);
                        }
                    }
                }
            } else {
                // just retrieve with no instance IDs
                for (AnalysisMeasure analysisMeasure : measures) {
                    DataSet childSet = CloudWatchUtil.getDataSet(key, secretKey, null, analysisMeasure, dimensions, startDate, endDate,
                            period, analysisDateDimension);
                    for (IRow row : childSet.getRows()) {
                        dataSet.addRow(row);
                    }
                }
            }
        } else {
            // just retrieve via EC2Util
            List<EC2Info> ec2Infos = EC2Util.getInstances(key, secretKey);
            DataSet childSet = EC2Util.createDataSet(ec2Infos, dimensions);
            for (IRow row : childSet.getRows()) {
                if (analysisDateDimension != null) {
                    DateValue dummyDateValue = new DateValue(endDate);
                    row.addValue(analysisDateDimension.createAggregateKey(), dummyDateValue);
                }
                dataSet.addRow(row);
            }
        }
        return dataSet;
    }
}
