package test.core;

import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.Row;
import com.easyinsight.api.*;
import com.easyinsight.security.SecurityUtil;
import test.util.TestUtil;

import java.util.Arrays;
import java.util.Calendar;

/**
 * User: James Boe
 * Date: Jun 2, 2009
 * Time: 2:43:28 PM
 */
public class DateBlah {
    public static void main(String[] args) {
        Database.initialize();
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
        UserUploadService uploadService = new UserUploadService();
        long dataSourceID = uploadService.createNewDefaultFeed("Default Data Source");
        AnalysisMeasure countMeasure = new AnalysisMeasure("Count", AggregationTypes.SUM);
        AnalysisDateDimension date = new AnalysisDateDimension("Date", true, AnalysisDateDimension.DAY_LEVEL);
        AnalysisDimension grouping = new AnalysisDimension("Grouping", true);
        FeedDefinition feedDefinition = new FeedService().getFeedDefinition(dataSourceID);
        UploadPolicy uploadPolicy = feedDefinition.getUploadPolicy();
        uploadPolicy.setPubliclyVisible(true);
        uploadPolicy.setMarketplaceVisible(true);
        feedDefinition.setUploadPolicy(uploadPolicy);
        feedDefinition.setFields(Arrays.asList(countMeasure, date, grouping));
        feedDefinition.setPubliclyVisible(true);
        feedDefinition.setMarketplaceVisible(true);
        new FeedService().updateFeedDefinition(feedDefinition);
        ValidatingPublishService validatingPublishService = new ValidatingPublishService() {

            protected String getUserName() {
                return SecurityUtil.getSecurityProvider().getUserPrincipal().getUserName();
            }

            protected long getAccountID() {
                return SecurityUtil.getAccountID();
            }

            protected long getUserID() {
                return SecurityUtil.getUserID();
            }
        };
        com.easyinsight.api.Row row1 = createRow("X", 1, 2);
        com.easyinsight.api.Row row2 = createRow("X", 2, 2);
        com.easyinsight.api.Row row3 = createRow("X", 3, 2);
        com.easyinsight.api.Row row4 = createRow("Y", 1, 1);
        com.easyinsight.api.Row row5 = createRow("Y", 2, 1);
        com.easyinsight.api.Row row6 = createRow("Y", 3, 1);
        validatingPublishService.addRows(feedDefinition.getApiKey(), new com.easyinsight.api.Row[] { row1, row2, row3, row4, row5, row6 });
    }

    private static com.easyinsight.api.Row createRow(String grouping, int daysAgo, int count) {
        Calendar cal = Calendar.getInstance();
        com.easyinsight.api.Row row = new com.easyinsight.api.Row();
        StringPair stringPair = new StringPair();
        stringPair.setKey("Grouping");
        stringPair.setValue(grouping);
        NumberPair numberPair = new NumberPair();
        numberPair.setKey("Count");
        numberPair.setValue(count);
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo);
        DatePair datePair = new DatePair();
        datePair.setKey("Date");
        datePair.setValue(cal.getTime());
        row.setStringPairs(new StringPair[] { stringPair });
        row.setNumberPairs(new NumberPair[] { numberPair });
        row.setDatePairs(new DatePair[] { datePair });
        return row;
    }
}
