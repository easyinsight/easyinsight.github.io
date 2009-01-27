package test.core;

import junit.framework.TestCase;
import com.easyinsight.storage.DataRetrieval;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.database.Database;
import com.easyinsight.userupload.*;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.analysis.TagCloud;

import java.io.*;
import java.util.List;
import java.util.Arrays;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Apr 26, 2008
 * Time: 5:26:26 PM
 */
public class DataRetrievalTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testRetrieval() throws IOException {
        File file = new File("c:/tmpcsv/fred.csv");
        FileInputStream fis = new FileInputStream(file);
        byte[] array = new byte[(int) file.length()];
        int size = fis.read(array);
        if (size != file.length()) {
            throw new RuntimeException();
        }

        DataRetrieval dataRetrieval = new DataRetrieval();
        dataRetrieval.deleteData(-1);
        UserUploadService userUploadService = new UserUploadService();

        long uploadID = userUploadService.addRawUploadData(TestUtil.getTestUser(), "blah.csv", array);
        UploadFormat uploadFormat = new FlatFileUploadFormat(",", "");
        UserUploadAnalysis userUploadAnalysis = userUploadService.attemptParse(uploadID, uploadFormat);
        long createdFeedID = userUploadService.parsed(uploadID, uploadFormat, "TestFredFeed", "Testing", userUploadAnalysis.getFields(),
                new UploadPolicy(), new TagCloud());
        Feed feed = FeedRegistry.instance().getFeed(createdFeedID);
        List columns = Arrays.asList( TestUtil.createKey("Trade Weighted Exchange Index: Other Important Trading Partners - Index Jan 1997=100", createdFeedID),
            TestUtil.createKey("All Employees: Financial Activities - Thous.", createdFeedID),
                TestUtil.createKey("Date", createdFeedID));
        long worstLoadTimeStart = System.currentTimeMillis();
        DataSet dataSet = feed.getDataSet(columns, null, false, null);
        long elapsedTime = System.currentTimeMillis() - worstLoadTimeStart;
        System.out.println(elapsedTime);
        //System.out.println(dataSet.getFields());        
        System.out.println(dataSet.getRows().size());
        long preCacheTime = System.currentTimeMillis();
        dataSet = feed.getDataSet(columns, null, false, null);
        long postCacheTime = System.currentTimeMillis();
        System.out.println(postCacheTime - preCacheTime);
        System.out.println(dataSet.getRows().size());
        /*TestDataSetCreator testDataSetCreator = new TestDataSetCreator(5000);
        DataSet dataSet = testDataSetCreator.createDataSet();
        dataRetrieval.storeData(-1, dataSet);
        DataSet retrievedDataSet = dataRetrieval.retrieveDataSet(-1);*/
        
    }
}
