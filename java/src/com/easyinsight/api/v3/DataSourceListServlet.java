package com.easyinsight.api.v3;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONArray;
import org.json.JSONObject;

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
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();
        JSONArray array = new JSONArray();
        responseObject.put("data_sources", array);
        String filter = request.getParameter("filter");
        boolean zapierFilter = "zapier".equals(filter);
        boolean apiFilter = "api".equals(filter);
        java.util.List<DataSourceDescriptor> dataSources = new com.easyinsight.datafeeds.FeedService().searchForSubscribedFeeds();
        ExportMetadata md = ExportService.createExportMetadata(SecurityUtil.getAccountID(), conn, new InsightRequestMetadata());
        for (DataSourceDescriptor ds : dataSources) {
            if (zapierFilter) {
                if (ds.getDataSourceType() == FeedType.BLANK.getType()) {
                    JSONObject dsObject = ds.toJSON(md);
                    array.put(dsObject);
                }
            } else if (apiFilter) {
                if (ds.getDataSourceType() == FeedType.STATIC.getType() || ds.getDataSourceType() == FeedType.DEFAULT.getType()) {
                    JSONObject dsObject = ds.toJSON(md);
                    array.put(dsObject);
                }
            } else {

                JSONObject dsObject = ds.toJSON(md);
                array.put(dsObject);

            }

        }
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }
}
