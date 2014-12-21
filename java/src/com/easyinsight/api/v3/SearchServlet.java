package com.easyinsight.api.v3;

import com.easyinsight.analysis.AnalysisService;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.dashboard.DashboardStorage;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.PreferencesService;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/20/14
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(value = "/search", asyncSupported = true)
public class SearchServlet extends JSONServlet {

    @Override
    protected ResponseInfo processGet(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        String search = request.getParameter("query");
        ExportMetadata md = ExportService.createExportMetadata(conn);
        List<DataSourceDescriptor> descriptors = new FeedService().searchForSubscribedFeeds(conn);
        List<JSONObject> arr = descriptors.stream().filter((a) -> a.contains(search)).map((a) -> {
            try {
                return a.toJSON(md);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        List<DashboardDescriptor> dashboards = new DashboardStorage().getDashboards(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn, FeedService.testAccountVisible(conn)).values();
        List<JSONObject> arr2 = dashboards.stream().filter((a) -> a.contains(search)).map((a) -> {
            try {
                return a.toJSON(md);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        arr.addAll(arr2);
        Collection<InsightDescriptor> arr3 = new AnalysisService().getInsightDescriptors();
        arr.addAll(arr3.stream().filter((a) -> a.contains(search)).map((a) -> {
            try {
                return a.toJSON(md);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        arr = arr.subList(0, Math.min(10, arr.size()));
        JSONObject responseObject = new JSONObject();
        JSONArray ja = new JSONArray(arr);
        responseObject.put("results", ja);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
