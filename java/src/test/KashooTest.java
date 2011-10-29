package test;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.kashoo.KashooCompositeSource;
import com.easyinsight.datafeeds.zendesk.ZendeskCompositeSource;

/**
 * User: jamesboe
 * Date: 4/7/11
 * Time: 2:09 PM
 */
public class KashooTest extends DataSourceTesting {
    @Override
    protected void configure(FeedDefinition dataSource) {
        KashooCompositeSource zendeskCompositeSource = (KashooCompositeSource) dataSource;
        zendeskCompositeSource.setKsUserName("jboe@easy-insight.com");
        zendeskCompositeSource.setKsPassword("e@symone$");
    }

    @Override
    protected FeedType getType() {
        return FeedType.KASHOO_COMPOSITE;
    }

    public static void main(String[] args) throws Exception {
        KashooTest test = new KashooTest();
        test.testDataSource("jboe");
    }
}
