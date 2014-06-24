package com.easyinsight.api.v3;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.EIDescriptor;
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
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
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
}
