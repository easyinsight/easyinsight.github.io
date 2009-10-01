package test.core;

import junit.framework.TestCase;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import test.util.TestUtil;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: May 24, 2009
 * Time: 1:03:49 PM
 */
public class SaveAsTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testSaveAs() throws SQLException {
        TestUtil.getIndividualTestUser();
        AnalysisDimension blah = new AnalysisDimension("blah", true);
        AnalysisMeasure count = new AnalysisMeasure("count", AggregationTypes.SUM);
        AnalysisMeasure anotherCount = new AnalysisMeasure("anotherCount", AggregationTypes.SUM);
        long id = TestUtil.createTestDataSource(new DataSet(), Arrays.asList(blah, count, anotherCount));
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setDataFeedID(id);
        blah = (AnalysisDimension) TestUtil.getItem(id, "blah");
        count = (AnalysisMeasure) TestUtil.getItem(id, "count");
        FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
        List<Object> values = new ArrayList<Object>();
        values.add("Blah");
        filterValueDefinition.setFilteredValues(values);
        filterValueDefinition.setField(blah);
        listDefinition.setFilterDefinitions(Arrays.asList((FilterDefinition) filterValueDefinition));
        listDefinition.setColumns(Arrays.asList(blah, count));
        long reportID = new AnalysisService().saveAnalysisDefinition(listDefinition).getAnalysisID();
        new AnalysisService().saveAs(listDefinition, "Saved As Version");
    }
}
