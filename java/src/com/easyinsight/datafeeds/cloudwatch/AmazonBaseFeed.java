package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.quickbase.QuickbaseCompositeSource;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.ParseException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 4/7/11
 * Time: 2:59 PM
 */
public abstract class AmazonBaseFeed extends Feed {

    protected String getUserName(FeedDefinition parent) {
        CloudWatchDataSource cloudWatchDataSource = (CloudWatchDataSource) parent;
        return cloudWatchDataSource.getCwUserName();
    }

    protected String getPassword(FeedDefinition parent) {
        CloudWatchDataSource cloudWatchDataSource = (CloudWatchDataSource) parent;
        return cloudWatchDataSource.getCwPassword();
    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        // do we need a dimension?
        try {
            CloudWatchDataSource cloudWatchDataSource = (CloudWatchDataSource) new FeedStorage().getFeedDefinitionData(getDataSource().getParentSourceID(), conn);
            String key = cloudWatchDataSource.getCwUserName();
            String secretKey = cloudWatchDataSource.getCwPassword();
            Collection<AnalysisDimension> dimensions = new ArrayList<AnalysisDimension>();
            Collection<AnalysisMeasure> measures = new ArrayList<AnalysisMeasure>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (analysisItem.isDerived()) {
                    continue;
                }
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
            return retrieve(dimensions, measures, startDate, endDate, period, analysisDateDimension, key, secretKey);
        } catch (SignatureException se) {
            throw new ReportException(new DataSourceConnectivityReportFault("Please enter your key/secret key again.", getDataSource()));
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    protected abstract DataSet retrieve(Collection<AnalysisDimension> dimensions, Collection<AnalysisMeasure> measures, Date startDate, Date endDate, int period, AnalysisDateDimension analysisDateDimension, String key, String secretKey) throws Exception;
}
