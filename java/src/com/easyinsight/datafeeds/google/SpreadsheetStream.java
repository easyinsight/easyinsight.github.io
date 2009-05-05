package com.easyinsight.datafeeds.google;

import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.DynamicFeed;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.IRow;
import com.easyinsight.users.Credentials;
import com.easyinsight.core.NamedKey;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.util.ServiceException;

import java.net.URL;
import java.io.IOException;


/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 10:31:20 AM
 */
public class SpreadsheetStream extends DynamicFeed {

    public static final String WORKSHEET_URL = "worksheetURL";

    private String worksheetURL;

    public SpreadsheetStream() {
    }

    public SpreadsheetStream(String worksheetURL) {
        this.worksheetURL = worksheetURL;
    }

    public FeedType getDataFeedType() {
        return FeedType.GOOGLE;
    }
}
