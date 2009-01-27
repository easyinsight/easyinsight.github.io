package com.easyinsight.datafeeds.wesabe;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.users.Credentials;
import com.easyinsight.dataset.ColumnSegmentFactory;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataRetrievalManager;

/**
 * User: James Boe
 * Date: Aug 16, 2008
 * Time: 8:55:56 PM
 */
public class WesabeFeedDefinition extends FeedDefinition {
    public FeedType getFeedType() {
        return FeedType.WESABE;
    }

    public Feed createFeedObject() {
        return new WesabeFeed();
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.STANDARD_USERNAME_PW;
    }

    public CredentialsResponse refresh(Credentials credentials) {
        DataSet dataSet = WesabeDataProvider.refresh(credentials);
        ColumnSegmentFactory columnSegmentFactory = new ColumnSegmentFactory();
        PersistableDataSetForm persistable = columnSegmentFactory.createPersistableForm(dataSet, getFields());
        DataRetrievalManager.instance().storeData(getDataFeedID(), persistable);
        return new CredentialsResponse(true);
    }
}
