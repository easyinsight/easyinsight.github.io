package test.core;

import junit.framework.TestCase;
import com.easyinsight.stores.*;
import com.easyinsight.users.*;
import com.easyinsight.userupload.*;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedDescriptor;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.database.Database;
import com.easyinsight.storage.DataRetrieval;
import com.easyinsight.DataService;
import com.easyinsight.AnalysisDimension;
import com.easyinsight.AnalysisItem;
import com.easyinsight.AnalysisMeasure;
import com.easyinsight.webservice.google.ListDataResults;

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: May 28, 2008
 * Time: 12:11:29 PM
 */
public class SalesTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
        new DataRetrieval();
    }

    public void testStuff() {
        long testUserID = TestUtil.getTestUser();
        User user = new InternalUserService().retrieveUser(testUserID);
        StoreService storeService = new StoreService();
        Merchant merchant = new Merchant();
        merchant.setName("Test Merchant");

        // create the merchant acccount

        long merchantID = storeService.createMerchant(merchant);

        // create the feed with a commercial upload policy

        UserUploadService userUploadService = new UserUploadService();
        long dataFeedID = createDataFeed(user.getUserID(), merchantID, 10.00, userUploadService);

        // create the buyer

        User buyer = createBuyer();

        // locate the feed on a search

        FeedService feedService = new FeedService();
        List<FeedDescriptor> descriptors = feedService.searchForAvailableFeeds("Commercial Feed", null);

        FeedDescriptor feedDescriptor = findOurFeed(descriptors, dataFeedID);
        assertNotNull(feedDescriptor);
        /*CommercialUploadPolicy policy = (CommercialUploadPolicy) feedDescriptor.getPolicy();
        assertEquals(10.00, policy.getPrice().getCost(), .01);
        assertEquals(merchantID, policy.getMerchantID());

        // preview the data

        DataService dataService = new DataService();
        ListDataResults results = dataService.list(createListDefinition(buyer.getUserID(), dataFeedID), true);
        assertEquals(5, results.getRows().length);

        // buy the feed

        PurchaseManager purchaseManager = new PurchaseManager();
        purchaseManager.buy(dataFeedID, buyer, merchant, new Date());

        ListDataResults postBuyResults = dataService.list(createListDefinition(buyer.getUserID(), dataFeedID), false);
        assertEquals(6, postBuyResults.getRows().length);    */
    }

    private FeedDescriptor findOurFeed(List<FeedDescriptor> descriptors, long dataFeedID) {
        FeedDescriptor matchedDescriptor = null;
        for (FeedDescriptor feedDescriptor : descriptors) {
            if (feedDescriptor.getDataFeedID() == dataFeedID) {
                matchedDescriptor = feedDescriptor;
            }
        }
        return matchedDescriptor;
    }

    private User createBuyer() {
        UserService userService = new UserService();
        User user = new InternalUserService().retrieveUser("testbuyer");
        long userID;
        if (user == null) {
            User buyer = new User("testbuyer", "password", "James Boe", "testbuyer99@gmail.com");
            AccountTransferObject account = new AccountTransferObject();
            account.setAccountType(new IndividualAccount());
            userService.createAccount(buyer, account);
            userService.createAccount(buyer, account);
            userID = buyer.getUserID();
        } else {
            userID = user.getUserID();
        }
        user = new InternalUserService().retrieveUser(userID);
        return user;
    }

    private long createDataFeed(long accountID, long merchantID, double cost, UserUploadService userUploadService) {
        /*String csvText = "Customer,Product,Revenue\nAcme,WidgetX,500\nAcme,WidgetY,200" +
                "\nAnotherC,WidgetZ,500\nAnotherC,WidgetA,200\nC1,WX,500\nC2,WY,200";
        long uploadID = userUploadService.addRawUploadData(accountID, "sales.csv", csvText.getBytes());
        UserUploadAnalysis userUploadAnalysis = userUploadService.attemptParse(uploadID, new FlatFileUploadFormat(",", "\\,"));
        assertTrue(userUploadAnalysis.isSuccessful());
        assertTrue(userUploadAnalysis.getFields().size() == 3);
        CommercialUploadPolicy commercialUploadPolicy = new CommercialUploadPolicy();
        commercialUploadPolicy.setMerchantID(merchantID);
        Price price = new Price();
        price.setCost(cost);
        commercialUploadPolicy.setPrice(price);
        return userUploadService.parsed(uploadID, new FlatFileUploadFormat(",", "\\,"), "Commercial Feed", "Testing",
                Arrays.asList(new AnalysisMeasure("Revenue", AggregationTypes.SUM), new AnalysisDimension("Customer", true),
                        new AnalysisDimension("Product", true)), commercialUploadPolicy, new TagCloud());*/
        throw new UnsupportedOperationException();
    }

    private WSListDefinition createListDefinition(long accountID, long dataFeedID) {
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setName("Test List");
        listDefinition.setDataFeedID(dataFeedID);
        AnalysisDimension myDimension = new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true);
        List<AnalysisItem> analysisFields = new ArrayList<AnalysisItem>();
        analysisFields.add(myDimension);
        listDefinition.setColumns(analysisFields);
        return listDefinition;
    }
}
