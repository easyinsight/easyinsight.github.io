package com.easyinsight.pipeline;

import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
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
                                                     int timeWindow, InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        // the way this should work...

        // if date dimension and time window
        // make the query where the date is X and the filter is Y

        FilterDefinition rollingFilter = null;
        for (FilterDefinition filter : filters) {
            if (filter instanceof RollingFilterDefinition) {
                rollingFilter = filter;
                if (filter.getField() != null) {
                    filter.getField().setSort(2);
                }
            }
        }


        Date endDate = new Date();


        if (rollingFilter != null) {

            long time;
            if (timeWindow == 0) {
                time = 1000L * 60L * 60L * 24L * 2L;
            } else {
                time = 1000L * 60L * 60L * 24L * timeWindow;
            }
            Date startDate = new Date(endDate.getTime() - time);


            KPIValue startValue = blah(createReport(dataSourceID, measure, filters), startDate, measure, insightRequestMetadata, conn);
            KPIValue endValue = blah(createReport(dataSourceID, measure, filters), endDate, measure, insightRequestMetadata, conn);
            return Arrays.asList(startValue, endValue);
        } else {
            KPIValue value = blah(createReport(dataSourceID, measure, filters), endDate, measure, insightRequestMetadata, conn);
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
                         AnalysisMeasure measure, InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        insightRequestMetadata.setNow(endDate);
        DataSet result = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
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

    public List<KPIValue> calculateHistoricalValues(long dataSourceID, AnalysisMeasure analysisMeasure, List<FilterDefinition> filters, Date startDate, Date endDate) {
        return null;
    }
}
