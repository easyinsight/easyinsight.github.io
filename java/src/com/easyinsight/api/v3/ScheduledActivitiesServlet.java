package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.export.ScheduledActivity;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.AccountStats;
import com.easyinsight.users.AccountTransferObject;
import com.easyinsight.users.UserAccountAdminService;
import com.easyinsight.users.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/20/14
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledActivitiesServlet extends JSONServlet {


    @Override
    protected ResponseInfo processGet(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        ExportMetadata md = ExportService.createExportMetadata(conn);
        int offset = Integer.parseInt(request.getParameter("offset"));
        JSONObject jo = new JSONObject();
        Function<ScheduledActivity, JSONObject> f = (a) -> {
                    try {
                        return a.toJSON(md);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                };
        List<ScheduledActivity> l = new ExportService().getScheduledActivities(offset);

        jo.put("data_sources", new JSONArray(l.stream().sequential().filter((a) -> a.retrieveType() == ScheduledActivity.DATA_SOURCE_REFRESH).map(f).collect(Collectors.toList())));
        jo.put("reports", new JSONArray(l.stream().sequential().filter((a) -> a.retrieveType() == ScheduledActivity.REPORT_DELIVERY || a.retrieveType() == ScheduledActivity.GENERAL_DELIVERY).map(f).collect(Collectors.toList())));
        return new ResponseInfo(ResponseInfo.ALL_GOOD, jo.toString());
    }

    @Override
    protected ResponseInfo processPost(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject jo = new JSONObject();
        long id = Long.parseLong(request.getParameter("schedule_id"));
        int offset = Integer.parseInt(request.getParameter("offset"));
        ScheduledActivity sa = ScheduledActivity.fromJSON(id, jsonObject);
        ExportMetadata md = ExportService.createExportMetadata(conn);
        new ExportService().addOrUpdateSchedule(sa, offset, conn);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, jo.toString());
    }

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
