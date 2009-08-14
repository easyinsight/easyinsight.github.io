package test.dynamic;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.google.GoogleSpreadsheetAccess;
import com.easyinsight.datafeeds.google.GoogleFeedDefinition;
import com.easyinsight.datafeeds.google.GoogleDataProvider;
import com.easyinsight.users.Credentials;
import com.easyinsight.userupload.UserUploadService;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;
import java.io.IOException;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Feb 8, 2008
 * Time: 1:20:31 PM
 */
public class GoogleSpreadsheetTest extends TestCase {
    protected void setUp() throws Exception {
        super.setUp();
        Database.initialize();
    }

    public void testSpreadsheetRetrieval() throws IOException, ServiceException {
        TestUtil.getIndividualTestUser();
        Credentials credentials = new Credentials();
        credentials.setUserName("easyinsight99");
        credentials.setPassword("analyze123");
        GoogleDataProvider googleDataProvider = new GoogleDataProvider();
        googleDataProvider.testGoogleConnect(credentials, false);
        googleDataProvider.getAvailableGoogleSpreadsheets(credentials);
        URL feedUrl = new URL("http://spreadsheets.google.com/feeds/spreadsheets/private/full");
        SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(credentials);
        SpreadsheetFeed spreadsheetFeed = myService.getFeed(feedUrl, SpreadsheetFeed.class);
        String url = null;
        for (SpreadsheetEntry entry : spreadsheetFeed.getEntries()) {
            List<WorksheetEntry> worksheetEntries = entry.getWorksheets();
            for (WorksheetEntry worksheetEntry : worksheetEntries) {
                if (entry.getTitle().getPlainText().equals("Test Data")) {
                    url = worksheetEntry.getListFeedUrl().toString();
                }
            }
        }
        GoogleFeedDefinition definition = new GoogleFeedDefinition();
        definition.setWorksheetURL(url);
        UserUploadService userUploadService = new UserUploadService();
        long dataSourceID = userUploadService.newExternalDataSource(definition, credentials);
        userUploadService.refreshData(dataSourceID, credentials, false);
    }
}
