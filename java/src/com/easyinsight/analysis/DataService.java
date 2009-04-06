package com.easyinsight.analysis;

import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.users.Credentials;
import com.easyinsight.logging.LogClass;
import com.easyinsight.benchmark.BenchmarkManager;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;

import java.util.*;


/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 1:16:37 PM
 */

public class DataService implements IDataService {

    private FeedRegistry feedRegistry = FeedRegistry.instance();

    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem) {
        try {
            SecurityUtil.authorizeFeed(feedID, Roles.SUBSCRIBER);
            Feed feed = feedRegistry.getFeed(feedID);
            return feed.getMetadata(analysisItem);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public FeedMetadata getFeedMetadata(long feedID, boolean preview) {
        try {
            SecurityUtil.authorizeFeed(feedID, Roles.SUBSCRIBER);
            Feed feed = feedRegistry.getFeed(feedID);
            Collection<AnalysisItem> feedItems = feed.getFields();
            // need to apply renames from the com.easyinsight.analysis definition here?
            List<AnalysisItem> sortedList = new ArrayList<AnalysisItem>(feedItems);
            Collections.sort(sortedList, new Comparator<AnalysisItem>() {

                public int compare(AnalysisItem o1, AnalysisItem o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });
            AnalysisItem[] feedItemArray = new AnalysisItem[sortedList.size()];
            sortedList.toArray(feedItemArray);
            FeedMetadata feedMetadata = new FeedMetadata();
            feedMetadata.setDataSourceName(feed.getName());
            feedMetadata.setFields(feedItemArray);
            feedMetadata.setDataFeedID(feedID);
            feedMetadata.setVersion(feed.getVersion());
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

    public EmbeddedDataResults getEmbeddedResults(long reportID) {
        try {
            // TODO: check cache for report ID
            WSAnalysisDefinition analysisDefinition = new AnalysisService().openAnalysisDefinition(reportID);
            long startTime = System.currentTimeMillis();
            EmbeddedDataResults results;
            Feed feed = feedRegistry.getFeed(analysisDefinition.getDataFeedID());
            Set<Long> ids = validate(analysisDefinition, feed);
            if (ids.size() > 0) {
                throw new RuntimeException("Report is no longer valid.");
            } else {
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                Set<AnalysisItem> analysisItems = analysisDefinition.getColumnItems(feed.getFields());
                Collection<FilterDefinition> filters = analysisDefinition.getFilterDefinitions();
                DataSet dataSet = feed.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, feed.getFields(), false, null);
                ListDataResults listDataResults = dataSet.toList(analysisDefinition, feed.getFields(), insightRequestMetadata);
                results = new EmbeddedDataResults();
                results.setDefinition(analysisDefinition);
                results.setHeaders(listDataResults.getHeaders());
                results.setRows(listDataResults.getRows());
            }
            BenchmarkManager.recordBenchmark("DataService:List", System.currentTimeMillis() - startTime);
            return results;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public ListDataResults list(WSAnalysisDefinition analysisDefinition, boolean preview, InsightRequestMetadata insightRequestMetadata) {
        try {
            SecurityUtil.authorizeFeed(analysisDefinition.getDataFeedID(), Roles.SUBSCRIBER);
            long startTime = System.currentTimeMillis();
            ListDataResults results;
            Feed feed = feedRegistry.getFeed(analysisDefinition.getDataFeedID());
            Set<Long> ids = validate(analysisDefinition, feed);
            if (ids.size() > 0) {
                results = new ListDataResults();
                results.setInvalidAnalysisItemIDs(ids);
                results.setFeedMetadata(getFeedMetadata(analysisDefinition.getDataFeedID(), false));
            } else {
                Set<AnalysisItem> analysisItems = analysisDefinition.getColumnItems(feed.getFields());
                Collection<FilterDefinition> filters = analysisDefinition.getFilterDefinitions();
                DataSet dataSet = feed.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, feed.getFields(), false, null);
                results = dataSet.toList(analysisDefinition, feed.getFields(), insightRequestMetadata);
            }
            BenchmarkManager.recordBenchmark("DataService:List", System.currentTimeMillis() - startTime);
            return results;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public FeedMetadata getFeedMetadata(long dataFeed, Credentials credentials) {
        return getFeedMetadata(dataFeed, false);
    }

    public ListDataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        return list(analysisDefinition, false, insightRequestMetadata);
    }
}
