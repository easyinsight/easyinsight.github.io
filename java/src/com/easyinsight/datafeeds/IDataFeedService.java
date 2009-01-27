package com.easyinsight.datafeeds;

import java.util.List;

public interface IDataFeedService {

    List<FeedDescriptor> getMostPopularFeeds(String genreKey, int cutoff);

    List<FeedDescriptor> searchForSubscribedFeeds();

    List<FeedDescriptor> searchForAvailableFeeds(String keyword, String genreKey);

    InitialAnalysis getInitialAnalysisSetup(long dataFeedID);
}
