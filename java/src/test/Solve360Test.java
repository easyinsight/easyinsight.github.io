package test;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.kashoo.KashooCompositeSource;
import com.easyinsight.datafeeds.solve360.Solve360CompositeSource;

/**
 * User: jamesboe
 * Date: 4/7/11
 * Time: 2:09 PM
 */
public class Solve360Test extends DataSourceTesting {
    @Override
    protected void configure(FeedDefinition dataSource) {
        Solve360CompositeSource solve360CompositeSource = (Solve360CompositeSource) dataSource;
        solve360CompositeSource.setUserEmail("jboe@easy-insight.com");
        solve360CompositeSource.setAuthKey("GcT9Lekc47BbQ7U1u8f5HaI2v5+c44207cJeZ8W5");
    }

    @Override
    protected FeedType getType() {
        return FeedType.SOLVE360_COMPOSITE;
    }

    public static void main(String[] args) throws Exception {
        Solve360Test test = new Solve360Test();
        test.testDataSource("jboe");
    }
}
