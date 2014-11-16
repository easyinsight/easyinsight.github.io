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
 * Created by Alan on 11/11/14.
 */
public class MissingRefreshServlet extends JSONServlet {

    @Override
    protected ResponseInfo processGet(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        org.json.JSONObject jo = new org.json.JSONObject();
        ExportMetadata md = ExportService.createExportMetadata(conn);
        jo.put("refreshables", new JSONArray(new ExportService().getRefreshableDataSources(conn).stream().map((a) -> {
            try {
                return a.toJSON(md);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList())));
        return new ResponseInfo(ResponseInfo.ALL_GOOD, jo.toString());
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
