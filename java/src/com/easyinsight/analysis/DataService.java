package com.easyinsight.analysis;

import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.benchmark.BenchmarkManager;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.pipeline.Pipeline;
import com.easyinsight.pipeline.StandardReportPipeline;
import com.easyinsight.users.Credentials;

import java.util.*;
import java.io.Serializable;

import org.apache.jcs.JCS;


/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 1:16:37 PM
 */

public class DataService {

    private JCS reportCache = getCache("embeddedReports");

    private FeedRegistry feedRegistry = FeedRegistry.instance();

    private JCS getCache(String cacheName) {
        try {
            return JCS.getInstance(cacheName);
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem, List<CredentialFulfillment> credentials, int utfOffset) {
        SecurityUtil.authorizeFeedAccess(feedID);
        try {
            Feed feed = feedRegistry.getFeed(feedID);
            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setUtcOffset(utfOffset);
            insightRequestMetadata.setCredentialFulfillmentList(credentials);
            return feed.getMetadata(analysisItem, insightRequestMetadata);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public Set<CredentialRequirement> getCredentialRequirements(long feedID) {
        SecurityUtil.authorizeFeedAccess(feedID);
        try {
            Feed feed = feedRegistry.getFeed(feedID);
            return feed.getCredentialRequirement(false);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public FeedMetadata getFeedMetadata(long feedID) {
        SecurityUtil.authorizeFeedAccess(feedID);
        try {
            Feed feed = feedRegistry.getFeed(feedID);
            Set<CredentialRequirement> credentialRequirements = feed.getCredentialRequirement(false);
            Collection<AnalysisItem> feedItems = feed.getFields();
            // need to apply renames from the com.easyinsight.analysis definition here?
            List<AnalysisItem> sortedList = new ArrayList<AnalysisItem>(feedItems);
            Collections.sort(sortedList, new Comparator<AnalysisItem>() {

                public int compare(AnalysisItem o1, AnalysisItem o2) {
                    return o1.toDisplay().compareTo(o2.toDisplay());
                }
            });
            AnalysisItem[] feedItemArray = new AnalysisItem[sortedList.size()];
            sortedList.toArray(feedItemArray);
            FeedMetadata feedMetadata = new FeedMetadata();            
            feedMetadata.setCredentials(credentialRequirements);
            feedMetadata.setDataSourceName(feed.getName());
            feedMetadata.setFields(feedItemArray);
            feedMetadata.setFieldHierarchy(feed.getFieldHierarchy());
            feedMetadata.setIntrinsicFilters(feed.getIntrinsicFilters());
            feedMetadata.setDataFeedID(feedID);
            feedMetadata.setVersion(feed.getVersion());
            feedMetadata.setOriginSolution(feed.getOriginSolution());
            feedMetadata.setDataSourceAdmin(SecurityUtil.getRole(SecurityUtil.getUserID(false), feedID) == Roles.OWNER);
            return feedMetadata;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    /*public ListDataResults list(long analysisID) {
        AnalysisDefinition analysisDefinition = analysisStorage.getAnalysisDefinition(analysisID);
        return list(analysisDefinition.createBlazeDefinition());
    }

    public CrossTabDataResults pivot(long analysisID) {
        AnalysisDefinition analysisDefinition = analysisStorage.getAnalysisDefinition(analysisID);
        return pivot(analysisDefinition.createBlazeDefinition());
    } */

    private Set<Long> validate(WSAnalysisDefinition analysisDefinition, Feed feed) {
        Set<Long> ids = new HashSet<Long>();
        Set<Long> invalidIDs = new HashSet<Long>();
        for (AnalysisItem analysisItem : feed.getFields()) {
            ids.add(analysisItem.getKey().getKeyID());
        }
        Set<AnalysisItem> items = analysisDefinition.getAllAnalysisItems();
        for (AnalysisItem analysisItem : items) {
            if (analysisItem.getKey().getKeyID() != 0 && !ids.contains(analysisItem.getKey().getKeyID())) {
                invalidIDs.add(analysisItem.getKey().getKeyID());
            }
        }
        if (analysisDefinition.retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : analysisDefinition.retrieveFilterDefinitions()) {
                if (filterDefinition.getField().getKey().getKeyID() != 0 && !ids.contains(filterDefinition.getField().getKey().getKeyID())) {
                    invalidIDs.add(filterDefinition.getField().getKey().getKeyID());
                }
            }
        }
        return invalidIDs;
    }

    private static class EmbeddedCacheKey implements Serializable {
        
        private Collection<FilterDefinition> filters;
        private List<FilterDefinition> drillThroughFilters;
        private long reportID;

        private EmbeddedCacheKey(Collection<FilterDefinition> filters, long reportID, List<FilterDefinition> drillThroughFilters) {
            this.filters = filters;
            this.reportID = reportID;
            this.drillThroughFilters = drillThroughFilters;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EmbeddedCacheKey that = (EmbeddedCacheKey) o;

            if (reportID != that.reportID) return false;
            if (drillThroughFilters != null ? !drillThroughFilters.equals(that.drillThroughFilters) : that.drillThroughFilters != null)
                return false;
            if (filters != null ? !filters.equals(that.filters) : that.filters != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = filters != null ? filters.hashCode() : 0;
            result = 31 * result + (drillThroughFilters != null ? drillThroughFilters.hashCode() : 0);
            result = 31 * result + (int) (reportID ^ (reportID >>> 32));
            return result;
        }
    }

    public EmbeddedResults getEmbeddedResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters,
                                                  InsightRequestMetadata insightRequestMetadata, List<FilterDefinition> drillThroughFilters) {
        SecurityUtil.authorizeInsight(reportID);
        try {
            Feed feed = feedRegistry.getFeed(dataSourceID);
            Set<CredentialRequirement> credentialsRequired = feed.getCredentialRequirement(insightRequestMetadata.isRefreshAllSources());
            if (credentialsRequired.size() > 0) {
                int count = 0;
                for (CredentialRequirement credentialRequirement : credentialsRequired) {
                    if (insightRequestMetadata.getCredentialForDataSource(credentialRequirement.getDataSourceID()) != null) {
                        count++;
                    }
                }
                if (count < credentialsRequired.size()) {
                    EmbeddedResults embeddedResults = new EmbeddedDataResults();
                    embeddedResults.setCredentialRequirements(credentialsRequired);
                    return embeddedResults;
                }
            }
            if (insightRequestMetadata.isRefreshAllSources()) {
                List<Long> containedIDs = feed.getDataSourceIDs();
                for (Long containedID : containedIDs) {
                    FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(containedID);
                    if (feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PULL ||
                            feedDefinition.getDataSourceType() == DataSourceInfo.COMPOSITE_PULL) {
                        IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedDefinition;
                        Credentials credentials = insightRequestMetadata.getCredentialForDataSource(feedDefinition.getDataFeedID());                        
                        dataSource.refreshData(credentials, SecurityUtil.getAccountID(), new Date(), null);
                    }
                }
            }
            EmbeddedCacheKey key = new EmbeddedCacheKey(customFilters, reportID, drillThroughFilters);
            Map<EmbeddedCacheKey, EmbeddedResults> resultsCache = null;
            if (!insightRequestMetadata.isNoCache() && reportCache != null) {
                //noinspection unchecked
                resultsCache = (Map<EmbeddedCacheKey, EmbeddedResults>) reportCache.get(dataSourceID);
                if (resultsCache != null) {
                    EmbeddedResults results = resultsCache.get(key);
                    if (results != null) {
                        results = results.clone();
                        boolean dataSourceAccessible;
                        try {
                            SecurityUtil.authorizeFeedAccess(results.getDefinition().getDataFeedID());
                            dataSourceAccessible = true;
                        } catch (Exception e) {
                            dataSourceAccessible = false;
                        }
                        results.setDataSourceAccessible(dataSourceAccessible);
                        return results;
                    }
                } else {
                    resultsCache = new HashMap<EmbeddedCacheKey, EmbeddedResults>();
                }
            }
            WSAnalysisDefinition analysisDefinition = new AnalysisService().openAnalysisDefinition(reportID);
            boolean dataSourceAccessible;
            try {
                SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
                dataSourceAccessible = true;
            } catch (Exception e) {
                dataSourceAccessible = false;
            }
            if (customFilters != null) {
                analysisDefinition.setFilterDefinitions(customFilters);
            }
            if (drillThroughFilters != null) {
                analysisDefinition.applyFilters(drillThroughFilters);
            }

            long startTime = System.currentTimeMillis();      
            EmbeddedResults results;
            /*Set<Long> ids = validate(analysisDefinition, feed);
            if (ids.size() > 0) {
                throw new RuntimeException("Report is no longer valid.");
            } else {*/

                if (insightRequestMetadata.getHierarchyOverrides() != null) {
                    for (HierarchyOverride hierarchyOverride : insightRequestMetadata.getHierarchyOverrides()) {
                        for (AnalysisItem analysisItem : analysisDefinition.getAllAnalysisItems()) {
                            if (analysisItem.getAnalysisItemID() == hierarchyOverride.getAnalysisItemID()) {
                                AnalysisHierarchyItem hierarchy = (AnalysisHierarchyItem) analysisItem;
                                hierarchy.setHierarchyLevel(hierarchy.getHierarchyLevels().get(hierarchyOverride.getPosition()));
                            }
                        }
                    }
                }
                Set<AnalysisItem> analysisItems = analysisDefinition.getColumnItems(feed.getFields());
                Collection<FilterDefinition> filters = analysisDefinition.retrieveFilterDefinitions();
                boolean aggregateQuery = true;
                for (AnalysisItem analysisItem : analysisDefinition.getAllAnalysisItems()) {
                    if (analysisItem.blocksDBAggregation()) {
                        aggregateQuery = false;
                    }
                }
                insightRequestMetadata.setAggregateQuery(aggregateQuery);
                DataSet dataSet = feed.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, feed.getFields(), false);
                Pipeline pipeline = new StandardReportPipeline();
                pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
                // todo: fix
                DataResults listDataResults = pipeline.toList(dataSet);
                results = listDataResults.toEmbeddedResults();
                results.setDataSourceAccessible(dataSourceAccessible);
                results.setDefinition(analysisDefinition);
                ReportMetrics reportMetrics = new AnalysisStorage().getRating(reportID);
                results.setRatingsAverage(reportMetrics.getAverage());
                results.setRatingsCount(reportMetrics.getCount());
                if (dataSet.getLastTime() == null) {
                    dataSet.setLastTime(new Date());
                }
                DataSourceInfo dataSourceInfo = feed.getDataSourceInfo();
                dataSourceInfo.setLastDataTime(dataSet.getLastTime());
                results.setDataSourceInfo(dataSourceInfo);
                results.setAttribution(feed.getAttribution());
                if (resultsCache != null) {
                    resultsCache.put(key, results);                    
                    reportCache.put(dataSourceID, resultsCache);
                }
            //}
            BenchmarkManager.recordBenchmark("DataService:List", System.currentTimeMillis() - startTime);
            return results;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public DataSet listDataSet(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        try {
            Feed feed = feedRegistry.getFeed(analysisDefinition.getDataFeedID());
            Set<AnalysisItem> analysisItems = analysisDefinition.getColumnItems(feed.getFields());
            Set<AnalysisItem> validQueryItems = new HashSet<AnalysisItem>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (!analysisItem.isDerived()) {
                    validQueryItems.add(analysisItem);
                }
            }
            boolean aggregateQuery = true;
            for (AnalysisItem analysisItem : analysisDefinition.getAllAnalysisItems()) {
                if (analysisItem.blocksDBAggregation()) {
                    aggregateQuery = false;
                }
            }
            insightRequestMetadata.setAggregateQuery(aggregateQuery);
            Collection<FilterDefinition> filters = analysisDefinition.retrieveFilterDefinitions();
            DataSet dataSet = feed.getAggregateDataSet(validQueryItems, filters, insightRequestMetadata, feed.getFields(), false);
            Pipeline pipeline = new StandardReportPipeline();
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
            return pipeline.toDataSet(dataSet);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        try {
            long startTime = System.currentTimeMillis();
            Feed feed = feedRegistry.getFeed(analysisDefinition.getDataFeedID());
            if (insightRequestMetadata == null) {
                insightRequestMetadata = new InsightRequestMetadata();
            }
            Set<CredentialRequirement> credentialsRequired = feed.getCredentialRequirement(insightRequestMetadata.isRefreshAllSources());
            if (credentialsRequired.size() > 0) {
                int count = 0;
                for (CredentialRequirement credentialRequirement : credentialsRequired) {
                    if (insightRequestMetadata.getCredentialForDataSource(credentialRequirement.getDataSourceID()) != null) {
                        count++;
                    }
                }
                if (count < credentialsRequired.size()) {
                    ListDataResults embeddedDataResults = new ListDataResults();
                    embeddedDataResults.setCredentialRequirements(credentialsRequired);
                    return embeddedDataResults;
                }
            }
            if (insightRequestMetadata.isRefreshAllSources()) {
                List<Long> containedIDs = feed.getDataSourceIDs();
                for (Long containedID : containedIDs) {
                    FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(containedID);
                    if (feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PULL ||
                            feedDefinition.getDataSourceType() == DataSourceInfo.COMPOSITE_PULL) {
                        IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedDefinition;
                        Credentials credentials = insightRequestMetadata.getCredentialForDataSource(feedDefinition.getDataFeedID());
                        dataSource.refreshData(credentials, SecurityUtil.getAccountID(), new Date(), null);
                    }
                }
            }
            DataResults results;

            /*Set<Long> ids = validate(analysisDefinition, feed);
            if (ids.size() > 0) {
                results = new ListDataResults();
                results.setInvalidAnalysisItemIDs(ids);
                results.setFeedMetadata(getFeedMetadata(analysisDefinition.getDataFeedID(), false));
            } else {*/
            Set<AnalysisItem> analysisItems = analysisDefinition.getColumnItems(feed.getFields());
            Set<AnalysisItem> validQueryItems = new HashSet<AnalysisItem>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (!analysisItem.isDerived()) {
                    validQueryItems.add(analysisItem);
                }
            }
            boolean aggregateQuery = true;
            for (AnalysisItem analysisItem : analysisDefinition.getAllAnalysisItems()) {
                if (analysisItem.blocksDBAggregation()) {
                    aggregateQuery = false;
                }
            }
            insightRequestMetadata.setAggregateQuery(aggregateQuery);
            Collection<FilterDefinition> filters = analysisDefinition.retrieveFilterDefinitions();
            DataSet dataSet = feed.getAggregateDataSet(validQueryItems, filters, insightRequestMetadata, feed.getFields(), false);
            //results = dataSet.toList(analysisDefinition, feed.getFields(), insightRequestMetadata);
            Pipeline pipeline = new StandardReportPipeline();
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
            results = pipeline.toList(dataSet);
            DataSourceInfo dataSourceInfo = feed.getDataSourceInfo();
            if (dataSet.getLastTime() == null) {
                dataSet.setLastTime(new Date());
            }
            dataSourceInfo.setLastDataTime(dataSet.getLastTime());
            results.setDataSourceInfo(dataSourceInfo);
           // }
            BenchmarkManager.recordBenchmark("DataService:List", System.currentTimeMillis() - startTime);
            return results;
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Object>> getAllData(long dataSourceID, List<FilterDefinition> filterDefinitions) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        List<Map<String, Object>> objList = new ArrayList<Map<String, Object>>();
        try {
            Feed feed = feedRegistry.getFeed(dataSourceID);
            DataSet dataSet = feed.getDetails(filterDefinitions);
            for (IRow row : dataSet.getRows()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (Key key : row.getKeys()) {
                    Value value = row.getValue(key);
                    if (value == null) {
                        map.put(key.toDisplayName(), "");
                    } else {
                        map.put(key.toDisplayName(), value.toString());
                    }
                }
                objList.add(map);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return objList;
    }
}
