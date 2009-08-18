package test.core;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.dataset.DataSet;
import test.util.TestUtil;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * User: jamesboe
 * Date: Aug 18, 2009
 * Time: 9:48:14 AM
 */
public class MigrationTest extends TestCase {

    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
    }

    public void testDataSourceMigration() throws Exception {
        TestUtil.getIndividualTestUser();
        Key dim1Key = new NamedKey("dim1");
        AnalysisDimension dim1 = new AnalysisDimension(dim1Key, true);
        Key measure1Key = new NamedKey("measure1");
        AnalysisMeasure measure1 = new AnalysisMeasure(measure1Key, AggregationTypes.SUM);
        Key measure2Key = new NamedKey("measure2");
        AnalysisMeasure measure2 = new AnalysisMeasure(measure2Key, AggregationTypes.SUM);
        DataSet testDataSet = new DataSet();
        IRow row1 = testDataSet.createRow();
        row1.addValue(dim1Key, "XYZ");
        row1.addValue(measure1Key, 1);
        row1.addValue(measure2Key, 30);
        IRow row2 = testDataSet.createRow();
        row2.addValue(dim1Key, "XYZ");
        row2.addValue(measure1Key, 2);
        row2.addValue(measure2Key, 50);
        long id = TestUtil.createTestDataSource(testDataSet, Arrays.asList(dim1, measure1, measure2));
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(id);
        List<AnalysisItem> fields = feedDefinition.getFields();
        List<AnalysisItem> newFields = new ArrayList<AnalysisItem>();
        for (AnalysisItem field : fields) {
            if (field.getKey().equals(dim1Key) || field.getKey().equals(measure2Key)) {
                newFields.add(field);
            } else {
                AnalysisDimension newDimension = new AnalysisDimension();
                newDimension.setKey(field.getKey());
                newDimension.setGroup(true);
                newFields.add(newDimension);
            }
        }
        feedDefinition.setFields(newFields);
        new FeedService().updateFeedDefinition(feedDefinition, "", null);
        WSListDefinition list = new WSListDefinition();
        list.setColumns(Arrays.asList(TestUtil.getItem(id, "dim1"), TestUtil.getItem(id, "measure1"), TestUtil.getItem(id, "measure2")));
        list.setDataFeedID(id);
        DataSet dataSet = new DataService().listDataSet(list, new InsightRequestMetadata());
        assertEquals(2, dataSet.getRows().size());
    }
}
