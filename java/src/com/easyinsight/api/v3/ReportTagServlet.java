package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.tag.Tag;
import com.easyinsight.userupload.UserUploadService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/20/14
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportTagServlet extends JSONServlet {

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();
        List<Tag> tags = new UserUploadService().getReportTags();
        ExportMetadata md = ExportService.createExportMetadata(conn);
        JSONArray array;
        array = new JSONArray(tags.stream().map(d -> {
            try {
                return d.toJSON(md);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));


        responseObject.put("tags", array);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

}
