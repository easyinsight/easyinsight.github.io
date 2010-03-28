package test.core;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.userupload.*;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Jun 14, 2008
 * Time: 9:32:25 PM
 */
public class TagsTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testTags() throws SQLException {
        long userID = TestUtil.getIndividualTestUser();
        UserUploadService userUploadService = new UserUploadService();
        long dataFeedID = createFirstDataFeed(userID, userUploadService);
        FeedDefinition feedDefinition = userUploadService.getDataFeedConfiguration(dataFeedID);
        assertEquals(2, feedDefinition.getTags().size());
        feedDefinition.getTags().add(new Tag("ABC"));
        feedDefinition.getTags().add(new Tag("DEF"));
        feedDefinition.getTags().remove(new Tag("Blah"));
        userUploadService.updateFeedDefinition(feedDefinition);
        feedDefinition = userUploadService.getDataFeedConfiguration(dataFeedID);
        assertEquals(3, feedDefinition.getTags().size());
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setDataFeedID(dataFeedID);
        listDefinition.setColumns(Arrays.asList(new AnalysisDimension(TestUtil.createKey("Customer", dataFeedID), true),
                new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true), new AnalysisMeasure(TestUtil.createKey("Revenue", dataFeedID), AggregationTypes.SUM)));
        listDefinition.setName("Sample Analysis");
        TagCloud tagCloud = new TagCloud();
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(new Tag("Blah"));
        tags.add(new Tag("Foo"));
        tagCloud.setTags(tags);
        listDefinition.setTagCloud(tags);
        AnalysisService analysisService = new AnalysisService();
        long savedID = analysisService.saveAnalysisDefinition(listDefinition).getAnalysisID();
        WSAnalysisDefinition retrievedDefinition = analysisService.openAnalysisDefinition(savedID);
        assertEquals(2, retrievedDefinition.getTagCloud().size());
    }

    private long createFirstDataFeed(long accountID, UserUploadService userUploadService) throws SQLException {
        String csvText = "Customer,Product,Revenue\nAcme,WidgetX,500\nAcme,WidgetY,200";
        long uploadID = userUploadService.addRawUploadData(accountID, "test.csv", csvText.getBytes());
        /*UploadResponse uploadResponse = userUploadService.create(uploadID, "Test Feed");
        FeedStorage feedStorage = new FeedStorage();
        FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(uploadResponse.getFeedID());
        TagCloud tagCloud = new TagCloud();
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(new Tag("Blah"));
        tags.add(new Tag("Foo"));
        tagCloud.setTags(tags);
        feedDefinition.setTags(tags);
        feedStorage.updateDataFeedConfiguration(feedDefinition);
        return uploadResponse.getFeedID();*/
        return uploadID;
    }
}
