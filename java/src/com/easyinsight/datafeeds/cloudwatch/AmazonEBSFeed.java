package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.xerox.amazonws.ec2.Jec2;
import com.xerox.amazonws.ec2.VolumeInfo;

import java.util.*;

/**
 * User: jamesboe
 * Date: 4/4/11
 * Time: 9:51 PM
 */
public class AmazonEBSFeed extends AmazonBaseFeed {

    @Override
    protected DataSet retrieve(Collection<AnalysisDimension> dimensions, Collection<AnalysisMeasure> measures, Date startDate, Date endDate, int period, AnalysisDateDimension analysisDateDimension, String key, String secretKey) throws Exception {
        DataSet dataSet = new DataSet();
        if (measures.size() > 0) {
            if (dimensions.size() > 0) {
                // retrieve via instance IDs and aggregate
                List<VolumeInfo> infos = new Jec2(key, secretKey).describeVolumes(new ArrayList<String>());
                for (VolumeInfo info : infos) {
                    for (AnalysisMeasure analysisMeasure : measures) {
                        DataSet childSet = CloudWatchUtil.getEBSDataSet(key, secretKey, info, analysisMeasure, dimensions, startDate, endDate,
                                period, analysisDateDimension);
                        for (IRow row : childSet.getRows()) {
                            dataSet.addRow(row);
                        }
                    }
                }
            } else {
                // just retrieve with no instance IDs
                for (AnalysisMeasure analysisMeasure : measures) {
                    DataSet childSet = CloudWatchUtil.getEBSDataSet(key, secretKey, null, analysisMeasure, dimensions, startDate, endDate,
                            period, analysisDateDimension);
                    for (IRow row : childSet.getRows()) {
                        dataSet.addRow(row);
                    }
                }
            }
        } else {
            // just retrieve via EC2Util
            List<VolumeInfo> infos = new Jec2(key, secretKey).describeVolumes(new ArrayList<String>());
            DataSet childSet = EC2Util.createDataSetForEBS(infos, dimensions);
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
