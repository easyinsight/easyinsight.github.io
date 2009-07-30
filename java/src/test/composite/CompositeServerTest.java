package test.composite;

import junit.framework.TestCase;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.database.Database;
import com.easyinsight.users.Credentials;
import com.easyinsight.analysis.DataService;
import com.easyinsight.analysis.WSListDefinition;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.test.TestAlphaDataSource;
import com.easyinsight.datafeeds.test.TestBetaDataSource;
import com.easyinsight.datafeeds.test.TestCompositeDataSource;
import com.easyinsight.datafeeds.test.TestGammaDataSource;
import com.easyinsight.solutions.Solution;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.dataset.DataSet;

import java.util.Arrays;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 10:51:09 AM
 */
public class CompositeServerTest extends TestCase {

    public void setUp() {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testComposite() throws Exception {
        TestUtil.getIndividualTestUser();
        UserUploadService uploadService = new UserUploadService();
        TestCompositeDataSource source = new TestCompositeDataSource();
        long sourceID = uploadService.newExternalDataSource(source, new Credentials());
        DataService dataService = new DataService();
        WSListDefinition listDef = new WSListDefinition();
        listDef.setDataFeedID(sourceID);
        listDef.setColumns(Arrays.asList(TestUtil.getItem(sourceID, TestAlphaDataSource.DIM),
                TestUtil.getItem(sourceID, TestBetaDataSource.DIM),
                TestUtil.getItem(sourceID, TestGammaDataSource.PROJECT_NAME)));
        DataSet results = dataService.listDataSet(listDef, new InsightRequestMetadata());
        System.out.println("blah");
        /*Solution solution = new Solution();
        solution.setName("Blah");
        long solutionID = new SolutionService().addSolution(solution, Arrays.asList((int) sourceID));
        new SolutionService().installSolution(solutionID);*/
    }
}
