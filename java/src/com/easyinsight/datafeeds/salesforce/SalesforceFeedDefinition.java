package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.users.Credentials;
import com.easyinsight.userupload.CredentialsResponse;

/**
 * User: James Boe
 * Date: Jul 3, 2008
 * Time: 10:59:27 PM
 */
public class SalesforceFeedDefinition extends CompositeFeedDefinition {
    public FeedType getFeedType() {
        return FeedType.SALESFORCE;
    }

    public Feed createFeedObject() {
        //return new SalesforceFeed(getCompositeFeedNode());
        return null;
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.SALESFORCE;
    }

    public CredentialsResponse refresh(Credentials credentials) {
        //return SalesforceConnection.instance().refresh((SalesforceCredentials) credentials, getCompositeFeedNode());
        throw new UnsupportedOperationException();
    }
}
