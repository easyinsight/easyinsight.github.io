package com.easyinsight.datafeeds;

import com.easyinsight.api.v3.JSONServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.database.EIConnection;
import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/23/14
 * Time: 2:10 PM
 */
public class DataSourceSuggestionServlet extends JSONServlet {

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        List<DataSourceDescriptor> dataSources = new FeedService().searchForSubscribedFeeds();
        DataSourceDescriptor autoSource = null;
        for (DataSourceDescriptor dataSource : dataSources) {
            if (dataSource.getDataSourceType() == FeedType.COMPOSITE.getType()) {
                if (dataSource.isAutoCombined()) {
                    autoSource = dataSource;
                }
            }
        }
        new AutoComposite(autoSource.getId(), conn).doSomething();
        return null;
    }
}
