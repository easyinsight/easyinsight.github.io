package test.analysis;

import junit.framework.TestCase;
import junit.framework.Assert;
import com.easyinsight.analysis.URLPattern;
import com.easyinsight.analysis.Row;
import com.easyinsight.core.Key;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Sep 8, 2009
 * Time: 10:40:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class URLTests extends TestCase {

    public void testValidDynamicURL() {
        Assert.assertTrue(URLPattern.getKeys("http://www.easy-insight.com/app#feedid=[Feed ID]").contains("Feed ID"));
    }

    public void testCorrectDynamicURL() {
        Row row = new Row();
        row.addValue("Feed ID", "1");
        String url = URLPattern.getURL("http://www.easy-insight.com/app#feedid=[Feed ID]", row);
        Assert.assertEquals("http://www.easy-insight.com/app#feedid=1", url);
    }

    public void testTwoFields() {
        Row row = new Row();
        row.addValue("Location", "biz");
        row.addValue("Feed ID", "2");
        String url = URLPattern.getURL("http://www.easy-insight.[Location]/app#feedid=[Feed ID]", row);
        Assert.assertEquals("http://www.easy-insight.biz/app#feedid=2", url);
    }

    public void testInvalidField() {
        Row row = new Row();
        row.addValue("Location", "biz");
        row.addValue("Feed ID", "2");
        String url = URLPattern.getURL("http://www.[easy]-insight.[Location]/app#feedid=[Feed ID]", row);
        Assert.assertEquals("http://www.easy-insight.biz/app#feedid=2", url);
    }

}
