package com.easyinsight.pipeline;

import com.easyinsight.goals.GoalValue;
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
                                                     List<CredentialFulfillment> credentials) throws TokenMissingException {
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        Date startDate = cal.getTime();
        List<KPIValue> values = calculateHistoricalValues(dataSourceID, measure, filters, startDate, endDate, credentials);
        List<KPIValue> retValues = new ArrayList<KPIValue>();
        if (values.size() == 1) {
            retValues.add(values.get(0));
        } else if (values.size() == 2) {
            retValues.add(values.get(0));
            retValues.add(values.get(1));
        } else if (values.size() > 2) {
            retValues.add(values.get(values.size() - 2));
            retValues.add(values.get(values.size() - 1));
        }
        return retValues;
    }

    public List<KPIValue> calculateHistoricalValues(long dataSourceID, AnalysisMeasure measure, List<FilterDefinition> filters, Date startDate, Date endDate,
                                                     List<CredentialFulfillment> credentials) throws TokenMissingException {
        List<KPIValue> goalValues = new ArrayList<KPIValue>();
        Set<AnalysisItem> itemSet = new HashSet<AnalysisItem>();

        List<FilterDefinition> otherFilters = new ArrayList<FilterDefinition>();
        RollingFilterDefinition rollingFilterDefinition = null;
        Date realStartDate = startDate;
        for (FilterDefinition filterDefinition : filters) {
            if (filterDefinition instanceof RollingFilterDefinition) {

                rollingFilterDefinition = (RollingFilterDefinition) filterDefinition;
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                insightRequestMetadata.setNow(startDate);
                realStartDate = new Date(MaterializedRollingFilterDefinition.findStartDate(rollingFilterDefinition.getInterval(), startDate));
                if (rollingFilterDefinition.getInterval() == MaterializedRollingFilterDefinition.LAST_DAY) {
                    rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.DAY);
                }
            } else {
                otherFilters.add(filterDefinition);
            }
            itemSet.add(filterDefinition.getField());
        }
        Feed feed = FeedRegistry.instance().getFeed(dataSourceID);
        itemSet.addAll(measure.getAnalysisItems(feed.getFields(), Arrays.asList((AnalysisItem) measure), false));
        List<FilterDefinition> intrinsicFilters = feed.getIntrinsicFilters();
        for (FilterDefinition intrinsicFilter : intrinsicFilters) {
            if (intrinsicFilter instanceof RollingFilterDefinition) {
                FilterDateRangeDefinition dateRange = new FilterDateRangeDefinition();
                dateRange.setField(intrinsicFilter.getField());
                dateRange.setStartDate(realStartDate);
                dateRange.setEndDate(endDate);
                otherFilters.add(dateRange);
                itemSet.add(intrinsicFilter.getField());
            } else if (intrinsicFilter instanceof FilterDateRangeDefinition) {
                FilterDateRangeDefinition dateRange = (FilterDateRangeDefinition) intrinsicFilter;
                dateRange.setStartDate(realStartDate);
                dateRange.setEndDate(endDate);
                otherFilters.add(dateRange);
                itemSet.add(intrinsicFilter.getField());
            }
        }
        InsightRequestMetadata dataRequest = new InsightRequestMetadata();
        dataRequest.setCredentialFulfillmentList(credentials);
        DataSet dataSet = feed.getAggregateDataSet(itemSet, otherFilters, dataRequest, feed.getFields(), false, null);
        if (rollingFilterDefinition == null) {
            StandardReportPipeline pipeline = new StandardReportPipeline();
            WSListDefinition report = new WSListDefinition();
            report.setColumns(Arrays.asList((AnalysisItem) measure));
            report.setFilterDefinitions(filters);
            report.setDataFeedID(dataSourceID);
            DataSet result = pipeline.setup(report, feed, new InsightRequestMetadata()).toDataSet(dataSet);
            if (result.getRows().size() > 0) {
                IRow row = result.getRow(0);
                Value value = row.getValue(measure.createAggregateKey());
                KPIValue goalValue = new KPIValue();
                goalValue.setDate(new Date());
                goalValue.setValue(value.toDouble());
                goalValues.add(goalValue);
            }
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            long startTime = cal.getTimeInMillis();
            long endTime = endDate.getTime();
            for (long time = startTime; time < endTime;) {
                StandardReportPipeline pipeline = new StandardReportPipeline();
                WSListDefinition report = new WSListDefinition();
                report.setColumns(Arrays.asList((AnalysisItem) measure));
                report.setDataFeedID(dataSourceID);
                report.setFilterDefinitions(filters);
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                insightRequestMetadata.setNow(new Date(time));
                DataSet result = pipeline.setup(report, feed, insightRequestMetadata).toDataSet(dataSet);
                if (result.getRows().size() > 0) {
                    IRow row = result.getRow(0);
                    Value value = row.getValue(measure.createAggregateKey());
                    KPIValue goalValue = new KPIValue();
                    goalValue.setDate(cal.getTime());
                    goalValue.setValue(value.toDouble());
                    goalValues.add(goalValue);
                } else {
                    KPIValue goalValue = new KPIValue();
                    goalValue.setDate(cal.getTime());
                    goalValue.setValue(0);
                    goalValues.add(goalValue);
                }
                cal.add(Calendar.DAY_OF_YEAR, 1);
                time = cal.getTimeInMillis();
            }
        }
        return goalValues;
    }
}
