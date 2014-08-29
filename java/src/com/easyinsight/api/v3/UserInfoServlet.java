package com.easyinsight.api.v3;

import com.easyinsight.analysis.AnalysisService;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.InsightResponse;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.User;
import com.easyinsight.users.UserService;
import com.easyinsight.userupload.UserUploadService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/20/14
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserInfoServlet extends JSONServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        setBasicAuth(false);
    }


    @Override
    protected ResponseInfo processGet(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();
        User u = UserUploadService.retrieveUser(conn);
        ExportMetadata md = ExportService.createExportMetadata(conn);
        List<EIDescriptor> accountReports = new UserUploadService().getAccountReports();
        int count;
        PreparedStatement newsStmt = conn.prepareStatement("SELECT news_dismiss_date FROM USER WHERE user_id = ?");
        newsStmt.setLong(1, SecurityUtil.getUserID());
        ResultSet newsRS = newsStmt.executeQuery();
        newsRS.next();
        Timestamp dismissDate = newsRS.getTimestamp(1);
        count = 0;
        if (dismissDate != null) {
            PreparedStatement dateStmt = conn.prepareStatement("SELECT COUNT(NEWS_ENTRY_ID) FROM NEWS_ENTRY WHERE NEWS_ENTRY.entry_time > ?");
            dateStmt.setTimestamp(1, dismissDate);
            ResultSet rs = dateStmt.executeQuery();
            rs.next();
            count = rs.getInt(1);
            dateStmt.close();
        } else {

        }
        newsStmt.close();

        JSONArray arr = new JSONArray(accountReports.stream().map((d) -> {
            try {
                return d.toJSON(md);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        responseObject.put("bookmarks", arr);
        responseObject.put("user", u.toUserTransferObject().toJSON(md));
        responseObject.put("news_alert", count);

        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

    @Override
    protected ResponseInfo processPost(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();
        net.minidev.json.JSONArray bookmarks = (net.minidev.json.JSONArray) jsonObject.get("bookmarks");
        UserUploadService uus = new UserUploadService();
        ExportMetadata md = ExportService.createExportMetadata(conn);
        if (bookmarks != null) {
            for (Object o : bookmarks) {
                net.minidev.json.JSONObject jo = (net.minidev.json.JSONObject) o;
                EIDescriptor d = null;
                switch (String.valueOf(jo.get("type"))) {
                    case "dashboard":
                        d = new DashboardService().getDashboardDescriptor(String.valueOf(jo.get("url_key")), conn);
                        break;
                    case "report":
                        InsightResponse response = new AnalysisService().openAnalysisIfPossible(String.valueOf(jo.get("url_key")));
                        d = response.getInsightDescriptor();
                        break;
                    default:
                        d = null;
                }
                if (d != null) {
                    uus.addAccountReport(d, conn);
                    responseObject = d.toJSON(md);
                }
            }
        }
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

    @Override
    protected ResponseInfo processDelete(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();
        net.minidev.json.JSONArray bookmarks = (net.minidev.json.JSONArray) jsonObject.get("bookmarks");
        UserUploadService uus = new UserUploadService();
        if (bookmarks != null) {
            List<EIDescriptor> toDelete = new ArrayList<>();
            for (Object o : bookmarks) {
                net.minidev.json.JSONObject jo = (net.minidev.json.JSONObject) o;
                EIDescriptor d = null;
                switch (String.valueOf(jo.get("type"))) {
                    case "dashboard":
                        d = new DashboardService().getDashboardDescriptor(String.valueOf(jo.get("url_key")), conn);
                        break;
                    case "report":
                        InsightResponse response = new AnalysisService().openAnalysisIfPossible(String.valueOf(jo.get("url_key")));
                        d = response.getInsightDescriptor();
                        break;
                    default:
                        d = null;
                }
                if (d != null)
                    toDelete.add(d);
            }
            uus.deleteAccountReports(toDelete, conn);
        }
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
