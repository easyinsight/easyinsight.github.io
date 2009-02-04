package com.easyinsight.webservice.google;

import com.easyinsight.core.Value;
import com.easyinsight.FeedMetadata;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 5:04:09 PM
 */
public class CrossTabDataResults implements Serializable {
    private Collection<Map<String, Value>> results;
    private Set<Long> invalidAnalysisItemIDs;
    private FeedMetadata feedMetadata;

    public FeedMetadata getFeedMetadata() {
        return feedMetadata;
    }

    public void setFeedMetadata(FeedMetadata feedMetadata) {
        this.feedMetadata = feedMetadata;
    }

    public Set<Long> getInvalidAnalysisItemIDs() {
        return invalidAnalysisItemIDs;
    }

    public void setInvalidAnalysisItemIDs(Set<Long> invalidAnalysisItemIDs) {
        this.invalidAnalysisItemIDs = invalidAnalysisItemIDs;
    }
    
    public Collection<Map<String, Value>> getResults() {
        return results;
    }

    public void setResults(Collection<Map<String, Value>> results) {
        this.results = results;
    }
}
