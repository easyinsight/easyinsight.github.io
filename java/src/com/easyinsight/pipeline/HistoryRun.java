package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.CredentialFulfillment;
import com.easyinsight.core.Value;
import com.easyinsight.kpi.KPIValue;

import java.util.*;

/**
 * User: James Boe
 * Date: Jul 5, 2009
 * Time: 9:48:00 PM
 */
public class HistoryRun {

    public List<KPIValue> lastTwoValues(long dataSourceID, AnalysisMeasure measure, List<FilterDefinition> filters,
                                                     int timeWindow, InsightRequestMetadata insightRequestMetadata) throws TokenMissingException {
        // the way this should work...

        // if date dimension and time window
        // make the query where the date is X and the filter is Y

        FilterDefinition rollingFilter = null;
        for (FilterDefinition filter : filters) {
            if (filter instanceof RollingFilterDefinition) {
                rollingFilter = filter;
                filter.getField().setSort(2);
            }
        }

        Feed feed = FeedRegistry.instance().getFeed(dataSourceID);


        Date endDate = new Date();


        if (rollingFilter != null) {

            long time;
            if (timeWindow == 0) {
                time = 1000L * 60L * 60L * 24L * 2L;
            } else {
                time = 1000L * 60L * 60L * 24L * timeWindow;
            }
            Date startDate = new Date(endDate.getTime() - time);


            KPIValue startValue = blah(createReport(dataSourceID, measure, filters), startDate, measure, insightRequestMetadata);
            KPIValue endValue = blah(createReport(dataSourceID, measure, filters), endDate, measure, insightRequestMetadata);
            return Arrays.asList(startValue, endValue);
        } else {
            KPIValue value = blah(createReport(dataSourceID, measure, filters), endDate, measure, insightRequestMetadata);
            return Arrays.asList(value);
        }
    }

    public WSAnalysisDefinition createReport(long dataSourceID, AnalysisMeasure analysisMeasure, List<FilterDefinition> filters) {
        WSListDefinition report = new WSListDefinition();
        report.setDataFeedID(dataSourceID);
        report.setColumns(Arrays.asList((AnalysisItem) analysisMeasure));
        report.setFilterDefinitions(filters);
        return report;
    }

    public KPIValue blah(WSAnalysisDefinition analysisDefinition, Date endDate,
                         AnalysisMeasure measure, InsightRequestMetadata insightRequestMetadata) {
        insightRequestMetadata.setNow(endDate);
        DataSet result = new DataService().listDataSet(analysisDefinition, insightRequestMetadata);
        //results = dataSet.toList(analysisDefinition, feed.getFields(), insightRequestMetadata);
        KPIValue goalValue;
        if (result.getRows().size() > 0) {
            IRow row = result.getRow(0);
            Value value = row.getValue(measure.createAggregateKey());
            goalValue = new KPIValue();
            goalValue.setDate(endDate);
            goalValue.setValue(value.toDouble());
        } else {
            goalValue = new KPIValue();
            goalValue.setDate(endDate);
            goalValue.setValue(0);
        }
        return goalValue;
    }

    public List<KPIValue> calculateHistoricalValues(long dataSourceID, AnalysisMeasure analysisMeasure, List<FilterDefinition> filters, Date startDate, Date endDate, List<CredentialFulfillment> credentials) {
        Feed feed = FeedRegistry.instance().getFeed(dataSourceID);
        return null;
    }
}
