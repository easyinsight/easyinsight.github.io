package test;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.surveygizmo.SurveyGizmoCompositeSource;
import com.easyinsight.userupload.DataSourceThreadPool;

/**
 * User: jamesboe
 * Date: 4/7/11
 * Time: 2:09 PM
 */
public class SurveyGizmoTest extends DataSourceTesting {
    @Override
    protected void configure(FeedDefinition dataSource) {
        SurveyGizmoCompositeSource zendeskCompositeSource = (SurveyGizmoCompositeSource) dataSource;
        zendeskCompositeSource.setSgToken("jboe@easy-insight.com");
        zendeskCompositeSource.setSgSecret("asdf1234");
    }

    @Override
    protected FeedType getType() {
        return FeedType.SURVEYGIZMO_COMPOSITE;
    }

    public static void main(String[] args) throws Exception {
        DataSourceThreadPool.initialize();

        SurveyGizmoTest test = new SurveyGizmoTest();
        test.testDataSource("jaolen@gmail.com");
    }
}
