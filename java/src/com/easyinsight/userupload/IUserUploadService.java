package com.easyinsight.userupload;

import com.easyinsight.datafeeds.FeedDescriptor;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.AnalysisItem;
import com.easyinsight.analysis.TagCloud;

import java.util.List;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 10:10:29 PM
 */
public interface IUserUploadService {

    long addRawUploadData(long userID, String fileName, byte[] rawData);

    void subscribe(long dataFeedID);

    List<FeedDescriptor> getOwnedFeeds();

    FeedDefinition getDataFeedConfiguration(long dataFeedID);

    void updateFeedDefinition(FeedDefinition feedDefinition);

    void deleteUserUpload(long dataFeedID);
}
