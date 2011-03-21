package test.twitter;

import junit.framework.TestCase;
import com.easyinsight.datafeeds.twitter.TwitterDataSource;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.Database;

import java.util.*;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Dec 10, 2009
 * Time: 1:39:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TwitterSearchTest extends TestCase {
    private Map<String, Key> keys;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        keys = new HashMap<String, Key>();
        List<String> strings = Arrays.asList(TwitterDataSource.TWEET_ID, TwitterDataSource.PUBLISHED, TwitterDataSource.STATUS_LINK, TwitterDataSource.TITLE, TwitterDataSource.CONTENT, TwitterDataSource.UPDATED, TwitterDataSource.IMAGE_LOCATION, TwitterDataSource.SOURCE,
                TwitterDataSource.LANGUAGE, TwitterDataSource.AUTHOR_NAME, TwitterDataSource.AUTHOR_URL, TwitterDataSource.COUNT);
        for(String s : strings) {
            keys.put(s, new NamedKey(s));
        }
    }

    public void testTyLawsonSearch() {
        Database.initialize();
        Connection conn = Database.instance().getConnection();
        TwitterDataSource tds = new TwitterDataSource();
        tds.setSearches(new ArrayList(Arrays.asList("ty lawson")));
//        Feed feed = tds.createFeedObject();
        List<AnalysisItem> items;
        items = tds.createAnalysisItems(keys, conn);
        Set<AnalysisItem> itemSet = new HashSet<AnalysisItem>(items);
//        DataSet ds = feed.getAggregateDataSet(itemSet, null, null, items, false);

    }

}
