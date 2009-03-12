package com.easyinsight.datafeeds.google;

import com.easyinsight.datafeeds.google.Worksheet;
import com.easyinsight.users.Credentials;
import com.easyinsight.datafeeds.FeedDescriptor;

import java.util.List;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 1:27:50 PM
 */
public interface IGoogleStream {

    // get the available spreadsheets/worksheets

    // test a connection

    boolean testGoogleConnect(Credentials credentials);
}
