package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.DateValue;
import com.easyinsight.dataset.DataSet;
import com.xerox.amazonws.ec2.Jec2;
import com.xerox.amazonws.ec2.VolumeInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 4/4/11
 * Time: 9:51 PM
 */
public class AmazonRDSFeed extends AmazonBaseFeed {

    @Override
    protected DataSet retrieve(Collection<AnalysisDimension> dimensions, Collection<AnalysisMeasure> measures, Date startDate, Date endDate, int period, AnalysisDateDimension analysisDateDimension, String key, String secretKey) throws Exception {
        DataSet dataSet = new DataSet();

        if (measures.size() > 0) {
            if (dimensions.size() > 0) {
                // retrieve via instance IDs and aggregate
                List<String> infos = EC2Util.getDatabaseInstances(key, secretKey);
                for (String info : infos) {
                    for (AnalysisMeasure analysisMeasure : measures) {
                        DataSet childSet = CloudWatchUtil.getRDSDataSet(key, secretKey, info, analysisMeasure, dimensions, startDate, endDate,
                                period, analysisDateDimension);
                        for (IRow row : childSet.getRows()) {
                            dataSet.addRow(row);
                        }
                    }
                }
            } else {
                // just retrieve with no instance IDs
                for (AnalysisMeasure analysisMeasure : measures) {
                    DataSet childSet = CloudWatchUtil.getRDSDataSet(key, secretKey, null, analysisMeasure, dimensions, startDate, endDate,
                            period, analysisDateDimension);
                    for (IRow row : childSet.getRows()) {
                        dataSet.addRow(row);
                    }
                }
            }
        } else {
            // just retrieve via EC2Util
            List<String> infos = EC2Util.getDatabaseInstances(key, secretKey);
            DataSet childSet = EC2Util.createDataSetForRDS(infos, dimensions);
            for (IRow row : childSet.getRows()) {
                DateValue dummyDateValue = new DateValue(endDate);
                row.addValue(analysisDateDimension.createAggregateKey(), dummyDateValue);
                dataSet.addRow(row);
            }
        }
        return dataSet;
    }
}
