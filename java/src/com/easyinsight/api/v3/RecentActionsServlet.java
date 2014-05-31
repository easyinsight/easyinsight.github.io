package com.easyinsight.api.v3;

import com.easyinsight.admin.AdminService;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.audit.ActionDashboardLog;
import com.easyinsight.audit.ActionLog;
import com.easyinsight.audit.ActionReportLog;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/20/14
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecentActionsServlet extends JSONServlet {

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();

        ExportMetadata md = ExportService.createExportMetadata(SecurityUtil.getAccountID(), conn, new InsightRequestMetadata());
        Collection<ActionLog> actions = new AdminService().getRecentHTMLActions();
        JSONArray array = new JSONArray(actions.stream().sequential().filter(a ->
                (
                        (a instanceof ActionReportLog && a.getActionType() == ActionReportLog.VIEW) ||
                                (a instanceof ActionDashboardLog && a.getActionType() == ActionDashboardLog.VIEW))
        ).map(a -> {
            try {
                return a.toJSON(md);
            } catch(Exception e) {
                throw new RuntimeException();
            }
        }).toArray(JSONObject[]::new));
        responseObject.put("actions", array);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }
}
