package com.easyinsight.datafeeds.google;

import com.easyinsight.webservice.google.Worksheet;
import com.easyinsight.users.Credentials;
import com.easyinsight.datafeeds.FeedDescriptor;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 1:27:50 PM
 */
public interface IGoogleStream {

    // get the available spreadsheets/worksheets

    public Worksheet[] getAvailableGoogleSpreadsheets(Credentials credentials);

    // test a connection

    boolean testGoogleConnect(Credentials credentials);

    // create a feed from a worksheet

    FeedDescriptor createFeed(Credentials credentials, String title, String url);
}
