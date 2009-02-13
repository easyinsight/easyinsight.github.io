package com.easyinsight;

import com.easyinsight.webservice.google.CrossTabDataResults;
import com.easyinsight.webservice.google.ListDataResults;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.Crosstab;
import com.easyinsight.users.Credentials;
import com.easyinsight.core.Value;
import com.easyinsight.logging.LogClass;

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
            Feed feed = feedRegistry.getFeed(feedID);
            return feed.getMetadata(analysisItem);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public FeedMetadata getFeedMetadata(long feedID, boolean preview) {
        try {
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
            ids.add(analysisItem.getAnalysisItemID());
        }
        List<AnalysisItem> items = analysisDefinition.getAllAnalysisItems();
        for (AnalysisItem analysisItem : items) {
            if (analysisItem.getAnalysisItemID() != 0 && !ids.contains(analysisItem.getAnalysisItemID())) {
                invalidIDs.add(analysisItem.getAnalysisItemID());
            }
        }
        if (analysisDefinition.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : analysisDefinition.getFilterDefinitions()) {
                if (filterDefinition.getField().getAnalysisItemID() != 0 && !ids.contains(filterDefinition.getField().getAnalysisItemID())) {
                    invalidIDs.add(filterDefinition.getField().getAnalysisItemID());
                }
            }
        }
        return invalidIDs;
    }

    public ListDataResults list(WSAnalysisDefinition analysisDefinition, boolean preview, InsightRequestMetadata insightRequestMetadata) {
        try {
            ListDataResults results;
            Feed feed = feedRegistry.getFeed(analysisDefinition.getDataFeedID());
            Set<Long> ids = validate(analysisDefinition, feed);
            if (ids.size() > 0) {
                results = new ListDataResults();
                results.setInvalidAnalysisItemIDs(ids);
                results.setFeedMetadata(getFeedMetadata(analysisDefinition.getDataFeedID(), false));
            } else {
                DataSet dataSet = feed.getDataSet(analysisDefinition.getColumnKeys(feed.getFields()), preview ? 5 : null, false, insightRequestMetadata);
                results = dataSet.toList(analysisDefinition, feed.getFields(), insightRequestMetadata);
            }
            return results;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public CrossTabDataResults pivot(WSAnalysisDefinition analysisDefinition, boolean preview, InsightRequestMetadata insightRequestMetadata) {
        try {
            WSCrosstabDefinition crosstabDefinition = (WSCrosstabDefinition) analysisDefinition;
            Feed feed = feedRegistry.getFeed(crosstabDefinition.getDataFeedID());
            Set<Long> ids = validate(analysisDefinition, feed);
            CrossTabDataResults crossTabDataResults = new CrossTabDataResults();
            if (ids.size() > 0) {
                crossTabDataResults.setInvalidAnalysisItemIDs(ids);
                crossTabDataResults.setFeedMetadata(getFeedMetadata(analysisDefinition.getDataFeedID(), false));
            } else {
                DataSet dataSet = feed.getDataSet(analysisDefinition.getColumnKeys(feed.getFields()), preview ? 5 : null, false, insightRequestMetadata);
                Crosstab crosstab = dataSet.toCrosstab(crosstabDefinition, insightRequestMetadata);
                Collection<Map<String, Value>> results = crosstab.outputForm();
                crossTabDataResults.setResults(results);
            }
            return crossTabDataResults;
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

    public CrossTabDataResults pivot(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        return pivot(analysisDefinition, false, insightRequestMetadata);
    }
}
