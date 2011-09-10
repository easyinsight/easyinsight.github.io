package com.easyinsight.datafeeds;

import com.easyinsight.email.UserStub;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.util.RandomTextGenerator;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;

/**
 * User: James Boe
 * Date: Nov 9, 2008
 * Time: 9:22:36 PM
 */
public class FeedCreation {

    private FeedStorage feedStorage = new FeedStorage();

    public FeedCreationResult createFeed(FeedDefinition feedDefinition, Connection conn, @Nullable DataSet dataSet, UploadPolicy uploadPolicy) throws Exception {
        long feedID;
        if (feedDefinition.getDataFeedID() == 0) {
            feedID = feedStorage.addFeedDefinitionData(feedDefinition, conn);
        } else {
            feedID = feedDefinition.getDataFeedID();
            feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
        }
        long accountID = 0;
        for (FeedConsumer feedConsumer : uploadPolicy.getOwners()) {
            if (feedConsumer.type() == FeedConsumer.USER) {
                UserStub userStub = (UserStub) feedConsumer;
                accountID = userStub.getAccountID();
            }
        }
        DataStorage tableDef = null;
        if (dataSet != null && (feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PUSH || feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PULL)) {
            tableDef = DataStorage.writeConnection(feedDefinition, conn, accountID);
            tableDef.createTable();
            tableDef.insertData(dataSet);
        } else {
            DataStorage.liveDataSource(feedID, conn);
        }
        feedDefinition.setApiKey(RandomTextGenerator.generateText(12));
        feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
        return new FeedCreationResult(feedID, tableDef);
    }
}
