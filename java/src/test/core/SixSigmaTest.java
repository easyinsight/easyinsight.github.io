package test.core;

import junit.framework.TestCase;
import test.util.TestUtil;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.api.ValidatingPublishService;
import com.easyinsight.api.Row;
import com.easyinsight.api.StringPair;
import com.easyinsight.api.NumberPair;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;

import java.util.Arrays;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: May 1, 2009
 * Time: 10:10:40 AM
 */
public class SixSigmaTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testMeasure() throws SQLException {
        TestUtil.getIndividualTestUser();
        UserUploadService uploadService = new UserUploadService();
        long dataSourceID = uploadService.createNewDefaultFeed("Default Data Source");
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
        AnalysisDimension customer = new AnalysisDimension("customer", true);
        AnalysisMeasure defects = new AnalysisMeasure("defects", AggregationTypes.SUM);
        AnalysisMeasure opportunities = new AnalysisMeasure("opportunities", AggregationTypes.SUM);
        SixSigmaMeasure processSigma = createSigmaMeasure(defects, opportunities, SixSigmaMeasure.PROCESS_SIGMA, "ProcessSigma");
        SixSigmaMeasure defectsPercentage = createSigmaMeasure(defects, opportunities, SixSigmaMeasure.DEFECT_PERCENTAGE, "DefectPercentage");
        SixSigmaMeasure defectsPerMil = createSigmaMeasure(defects, opportunities, SixSigmaMeasure.DEFECTS_PER_MILLION_OPPS, "DefectsPerMillion");
        SixSigmaMeasure yieldPercentage = createSigmaMeasure(defects, opportunities, SixSigmaMeasure.YIELD_PERCENTAGE, "YieldPercentage");
        feedDefinition.setFields(Arrays.asList(customer, defects, opportunities, processSigma, defectsPercentage, defectsPerMil, yieldPercentage));
        new FeedService().updateFeedDefinition(feedDefinition, null);
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
        Row row1 = createRow();
        Row row2 = createRow();
        validatingPublishService.addRows(feedDefinition.getApiKey(), new Row[] { row1, row2 });
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setName("List");
        listDefinition.setColumns(Arrays.asList(getItem("customer", feedDefinition), getItem("ProcessSigma", feedDefinition),
                getItem("DefectPercentage", feedDefinition), getItem("DefectsPerMillion", feedDefinition), getItem("YieldPercentage", feedDefinition)));
        listDefinition.setDataFeedID(dataSourceID);
        ListDataResults results = (ListDataResults) new DataService().list(listDefinition, new InsightRequestMetadata());
        ListRow listRow = results.getRows()[0];
        
    }

    private SixSigmaMeasure createSigmaMeasure(AnalysisMeasure defects, AnalysisMeasure opportunities, int sigmaType, String name) {
        SixSigmaMeasure sixSigmaMeasure = new SixSigmaMeasure();
        sixSigmaMeasure.setKey(new NamedKey(name));
        sixSigmaMeasure.setSigmaType(sigmaType);
        sixSigmaMeasure.setTotalDefectsMeasure(defects);
        sixSigmaMeasure.setTotalOpportunitiesMeasure(opportunities);
        return sixSigmaMeasure;
    }

    private Row createRow() {
        Row row = new Row();
        StringPair stringPair = new StringPair();
        stringPair.setKey("customer");
        stringPair.setValue("c1");
        NumberPair numberPair = new NumberPair();
        numberPair.setKey("opportunities");
        numberPair.setValue(10);
        NumberPair defectPair = new NumberPair();
        defectPair.setKey("defects");
        defectPair.setValue(1);

        row.setStringPairs(new StringPair[] { stringPair });
        row.setNumberPairs(new NumberPair[] { numberPair, defectPair });
        return row;
    }

    private AnalysisItem getItem(String name, FeedDefinition feedDefinition) {
        for (AnalysisItem item : feedDefinition.getFields()) {
            if (item.getKey().toKeyString().equals(name)) {
                return item;
            }
        }
        throw new RuntimeException();
    }
}
