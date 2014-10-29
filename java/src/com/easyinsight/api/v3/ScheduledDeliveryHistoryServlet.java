package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import net.minidev.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 10/10/14
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledDeliveryHistoryServlet extends JSONServlet {

    @Override
    protected ResponseInfo processGet(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        long deliveryID = Long.parseLong(request.getParameter("deliveryID"));
        ExportMetadata md = ExportService.createExportMetadata(conn);
        JSONArray ja = new JSONArray(new ExportService().getReportDeliveryAudits(deliveryID).stream().map((a) -> {
            try {
                return a.toJSON(md);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        org.json.JSONObject jo = new org.json.JSONObject();
        jo.put("history", ja);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, jo.toString());
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
