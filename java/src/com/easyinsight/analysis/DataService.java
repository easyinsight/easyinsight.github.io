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

    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem, List<CredentialFulfillment> credentials) {
        try {
            SecurityUtil.authorizeFeedAccess(feedID);
            Feed feed = feedRegistry.getFeed(feedID);
            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setCredentialFulfillmentList(credentials);
            return feed.getMetadata(analysisItem, insightRequestMetadata);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public FeedMetadata getFeedMetadata(long feedID) {
        SecurityUtil.authorizeFeedAccess(feedID);
        try {
            Feed feed = feedRegistry.getFeed(feedID);
            List<CredentialRequirement> credentialRequirements = feed.getCredentialRequirement();
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
            feedMetadata.setIntrinsicFilters(feed.getIntrinsicFilters());
            feedMetadata.setDataFeedID(feedID);
            feedMetadata.setVersion(feed.getVersion());
            feedMetadata.setDataSourceAdmin(SecurityUtil.getRole(SecurityUtil.getUserID(), feedID) == Roles.OWNER);
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
        if (analysisDefinition.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : analysisDefinition.getFilterDefinitions()) {
                if (filterDefinition.getField().getKey().getKeyID() != 0 && !ids.contains(filterDefinition.getField().getKey().getKeyID())) {
                    invalidIDs.add(filterDefinition.getField().getKey().getKeyID());
                }
            }
        }
        return invalidIDs;
    }

    private static class EmbeddedCacheKey implements Serializable {
        private Collection<FilterDefinition> filters;
        private long reportID;

        private EmbeddedCacheKey(Collection<FilterDefinition> filters, long reportID) {
            this.filters = filters;
            this.reportID = reportID;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EmbeddedCacheKey that = (EmbeddedCacheKey) o;

            return reportID == that.reportID && !(filters != null ? !filters.equals(that.filters) : that.filters != null);

        }

        @Override
        public int hashCode() {
            int result = filters != null ? filters.hashCode() : 0;
            result = 31 * result + (int) (reportID ^ (reportID >>> 32));
            return result;
        }
    }

    public EmbeddedDataResults getEmbeddedResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters,
                                                  InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeInsight(reportID);
        try {
            EmbeddedCacheKey key = new EmbeddedCacheKey(customFilters, reportID);
            Map<EmbeddedCacheKey, EmbeddedDataResults> resultsCache = null;
            if (reportCache != null) {
                //noinspection unchecked
                resultsCache = (Map<EmbeddedCacheKey, EmbeddedDataResults>) reportCache.get(dataSourceID);
                if (resultsCache != null) {
                    EmbeddedDataResults results = resultsCache.get(key);
                    if (results != null) {
                        results = new EmbeddedDataResults(results);
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
                    resultsCache = new HashMap<EmbeddedCacheKey, EmbeddedDataResults>();
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
            long startTime = System.currentTimeMillis();
            EmbeddedDataResults results;
            Feed feed = feedRegistry.getFeed(analysisDefinition.getDataFeedID());
            /*Set<Long> ids = validate(analysisDefinition, feed);
            if (ids.size() > 0) {
                throw new RuntimeException("Report is no longer valid.");
            } else {*/
                Set<AnalysisItem> analysisItems = analysisDefinition.getColumnItems(feed.getFields());
                Collection<FilterDefinition> filters = analysisDefinition.getFilterDefinitions();
                DataSet dataSet = feed.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, feed.getFields(), false, null);
                Pipeline pipeline = new StandardReportPipeline();
                pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
                ListDataResults listDataResults = pipeline.toList(dataSet);
                results = new EmbeddedDataResults();
                results.setDataSourceAccessible(dataSourceAccessible);
                results.setDefinition(analysisDefinition);
                results.setHeaders(listDataResults.getHeaders());
                results.setRows(listDataResults.getRows());
                results.setLastDataTime(dataSet.getLastTime());
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

    public ListDataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        try {
            long startTime = System.currentTimeMillis();
            ListDataResults results;
            Feed feed = feedRegistry.getFeed(analysisDefinition.getDataFeedID());
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
            Collection<FilterDefinition> filters = analysisDefinition.getFilterDefinitions();
            DataSet dataSet = feed.getAggregateDataSet(validQueryItems, filters, insightRequestMetadata, feed.getFields(), false, null);
            //results = dataSet.toList(analysisDefinition, feed.getFields(), insightRequestMetadata);
            Pipeline pipeline = new StandardReportPipeline();
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
            results = pipeline.toList(dataSet);
           // }
            BenchmarkManager.recordBenchmark("DataService:List", System.currentTimeMillis() - startTime);
            return results;
        } catch (Exception e) {
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
