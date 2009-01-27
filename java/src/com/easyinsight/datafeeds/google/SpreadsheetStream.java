package com.easyinsight.datafeeds.google;

import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.DynamicFeed;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.IRow;
import com.easyinsight.users.Credentials;
import com.easyinsight.core.NamedKey;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.util.ServiceException;

import java.util.*;
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

    public void refresh(Credentials credentials) {
        DataSet dataSet;
        try {
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(credentials);
            URL listFeedUrl = new URL(this.worksheetURL);
            ListFeed feed = myService.getFeed(listFeedUrl, ListFeed.class);
            dataSet = new DataSet();
            for (ListEntry listEntry : feed.getEntries()) {
                IRow row = dataSet.createRow();
                for (String tag : listEntry.getCustomElements().getTags()) {
                    String value = listEntry.getCustomElements().getValue(tag);
                    row.addValue(new NamedKey(tag), value);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        
    }
}
