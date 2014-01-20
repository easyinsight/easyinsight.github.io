package com.easyinsight.api.v3;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/20/14
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSourceListServlet extends JSONServlet {

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();
        JSONArray array = new JSONArray();
        responseObject.put("data_sources", array);
        String filter = request.getParameter("filter");
        boolean zapierFilter = "zapier".equals(filter);
        java.util.List<DataSourceDescriptor> dataSources = new com.easyinsight.datafeeds.FeedService().searchForSubscribedFeeds();
        for(DataSourceDescriptor ds : dataSources) {
            if(!zapierFilter || (zapierFilter && (ds.getDataSourceType() == FeedType.STATIC.getType() || ds.getDataSourceType() == FeedType.DEFAULT.getType()))) {
                JSONObject dsObject = new JSONObject();
                dsObject.put("name", ds.getName());
                dsObject.put("url_key", ds.getUrlKey());
                array.add(dsObject);
            }

        }
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }
}
