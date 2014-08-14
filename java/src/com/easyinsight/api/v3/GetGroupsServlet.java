package com.easyinsight.api.v3;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.groups.GroupService;
import com.easyinsight.preferences.*;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.UserAccountAdminService;
import com.easyinsight.users.UserCreationResponse;
import com.easyinsight.users.UserTransferObject;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: jamesboe
 * Date: 2/19/14
 * Time: 12:58 PM
 */
public class GetGroupsServlet extends JSONServlet {

    @Override
    protected ResponseInfo processPost(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {

        return new ResponseInfo(200, "");
    }

    @Override
    protected ResponseInfo processGet(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        org.json.JSONObject result = new org.json.JSONObject();
        ExportMetadata md = ExportService.createExportMetadata(conn);
        List<GroupDescriptor> groups = new GroupService().getAccountGroups();
        result.put("groups", new org.json.JSONArray(groups.stream().map((a) -> {
            try {
                return a.toJSON(md);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList())));

        ResponseInfo responseInfo = new ResponseInfo(200, result.toString());
        return responseInfo;
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
