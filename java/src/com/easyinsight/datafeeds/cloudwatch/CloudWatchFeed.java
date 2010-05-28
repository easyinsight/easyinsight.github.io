package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.CredentialRequirement;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.DateValue;
import com.easyinsight.users.Credentials;
import com.easyinsight.logging.LogClass;

import java.util.*;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 3:32:14 PM
 */
public class CloudWatchFeed extends Feed {
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        if (analysisItem.getKey().toKeyString().equals(CloudWatchDataSource.DATE)) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -13);
            AnalysisDateDimensionResultMetadata dateMeta = new AnalysisDateDimensionResultMetadata();
            dateMeta.setEarliestDate(cal.getTime());
            dateMeta.setLatestDate(new Date());
            return dateMeta;
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            Credentials credentials = insightRequestMetadata.getCredentialForDataSource(getFeedID());
            if (credentials == null) {
                throw new RuntimeException();
            }
            try {
                List<EC2Info> ec2Infos = EC2Util.getInstances(credentials.getUserName(), credentials.getPassword());
                DataSet childSet = EC2Util.createDataSet(ec2Infos, Arrays.asList((AnalysisDimension) analysisItem));
                AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
                for (IRow row : childSet.getRows()) {
                    metadata.addValue(analysisItem, row.getValue(analysisItem.createAggregateKey()), insightRequestMetadata);
                }
                return metadata;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<FilterDefinition> getIntrinsicFilters() {
        FilterDateRangeDefinition rollingFilterDefinition = new FilterDateRangeDefinition();
        AnalysisItem dateField = null;
        for (AnalysisItem analysisItem : getFields()) {
            if (analysisItem.getKey().toKeyString().equals(CloudWatchDataSource.DATE)) {
                dateField = analysisItem;
            }
        }
        rollingFilterDefinition.setField(dateField);
        rollingFilterDefinition.setIntrinsic(true);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        rollingFilterDefinition.setStartDate(cal.getTime());
        rollingFilterDefinition.setEndDate(new Date());
        rollingFilterDefinition.setApplyBeforeAggregation(true);
        return Arrays.asList((FilterDefinition) rollingFilterDefinition);
    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode) throws TokenMissingException {
        // do we need a dimension?
        try {
            Credentials credentials = insightRequestMetadata.getCredentialForDataSource(getFeedID());
            if (credentials == null) {
                throw new RuntimeException();
            }
            Collection<AnalysisDimension> dimensions = new ArrayList<AnalysisDimension>();
            Collection<AnalysisMeasure> measures = new ArrayList<AnalysisMeasure>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                    measures.add((AnalysisMeasure) analysisItem);
                } else {
                    dimensions.add((AnalysisDimension) analysisItem);
                }
            }
            Date startDate = null;
            Date endDate = null;

            int period = 1000;
            AnalysisDateDimension analysisDateDimension = null;
            for (AnalysisDimension dimension : dimensions) {
                if (dimension.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    AnalysisDateDimension dateDimension = (AnalysisDateDimension) dimension;
                    analysisDateDimension = dateDimension;
                    switch (dateDimension.getDateLevel()) {
                        case AnalysisDateDimension.MINUTE_LEVEL:
                            period = 60;
                            break;
                        case AnalysisDateDimension.HOUR_LEVEL:
                            period = 60 * 60;
                            break;
                        case AnalysisDateDimension.DAY_LEVEL:
                        default:
                            period = 60 * 60 * 24;
                            break;
                    }
                }
            }
            for (FilterDefinition filterDefinition : filters) {
                if (filterDefinition.getField().getKey().toKeyString().equals(CloudWatchDataSource.DATE)) {
                    if (filterDefinition instanceof FilterDateRangeDefinition) {
                        FilterDateRangeDefinition dateRange = (FilterDateRangeDefinition) filterDefinition;
                        analysisDateDimension = (AnalysisDateDimension) dateRange.getField();
                        startDate = dateRange.getStartDate();
                        endDate = dateRange.getEndDate();
                    } else if (filterDefinition instanceof RollingFilterDefinition) {
                        RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filterDefinition;
                        analysisDateDimension = (AnalysisDateDimension) rollingFilterDefinition.getField();
                        endDate = insightRequestMetadata.getNow();
                        startDate = new Date(MaterializedRollingFilterDefinition.findStartDate(rollingFilterDefinition, endDate));
                    }
                }
            }
            DataSet dataSet = new DataSet();
            if (measures.size() > 0) {
                if (dimensions.size() > 0) {
                    // retrieve via instance IDs and aggregate
                    List<EC2Info> ec2Infos = EC2Util.getInstances(credentials.getUserName(), credentials.getPassword());
                    for (EC2Info info : ec2Infos) {
                        for (AnalysisMeasure analysisMeasure : measures) {
                            DataSet childSet = CloudWatchUtil.getDataSet(credentials.getUserName(), credentials.getPassword(), info, analysisMeasure, dimensions, startDate, endDate,
                                    period, analysisDateDimension);
                            for (IRow row : childSet.getRows()) {
                                dataSet.addRow(row);
                            }
                        }
                    }
                } else {
                    // just retrieve with no instance IDs
                    for (AnalysisMeasure analysisMeasure : measures) {
                        DataSet childSet = CloudWatchUtil.getDataSet(credentials.getUserName(), credentials.getPassword(), null, analysisMeasure, dimensions, startDate, endDate,
                                period, analysisDateDimension);
                        for (IRow row : childSet.getRows()) {
                            dataSet.addRow(row);
                        }
                    }
                }
            } else {
                // just retrieve via EC2Util
                List<EC2Info> ec2Infos = EC2Util.getInstances(credentials.getUserName(), credentials.getPassword());
                DataSet childSet = EC2Util.createDataSet(ec2Infos, dimensions);
                for (IRow row : childSet.getRows()) {
                    DateValue dummyDateValue = new DateValue(endDate);
                    row.addValue(CloudWatchDataSource.DATE, dummyDateValue);
                    dataSet.addRow(row);
                }
            }
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public Set<CredentialRequirement> getCredentialRequirement(boolean allSources) {
        Set<CredentialRequirement> credentials = super.getCredentialRequirement(allSources);
        CredentialRequirement requirement = new CredentialRequirement();
        requirement.setDataSourceID(getFeedID());
        requirement.setDataSourceName(getName());
        requirement.setCredentialsDefinition(CredentialsDefinition.STANDARD_USERNAME_PW);
        credentials.add(requirement);
        return credentials;
    }

    public DataSet getDetails(Collection<FilterDefinition> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
