package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.groups.Group;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.groups.GroupService;
import com.easyinsight.groups.GroupUser;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: jamesboe
 * Date: 2/19/14
 * Time: 12:58 PM
 */
public class GetGroupServlet extends JSONServlet {

    @Override
    protected ResponseInfo processPost(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        Group g = Group.fromJSON(jsonObject);
        long groupID = Long.parseLong(request.getParameter("group_id"));
        List<GroupUser> users = ((JSONArray) jsonObject.get("users")).stream().map(
                (a) -> GroupUser.fromJSON((JSONObject) a)
        ).collect(Collectors.toList());
        g.setGroupID(groupID);
        new GroupService().updateGroup(g);
        new GroupService().updateGroupUsers(groupID, users, g.isDataSourcesAutoIncludeChildren());
        JSONObject jo = new JSONObject();
        jo.put("success", true);
        return new ResponseInfo(200, jo.toString());
    }

    @Override
    protected ResponseInfo processGet(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        org.json.JSONObject result = new org.json.JSONObject();
        ExportMetadata md = ExportService.createExportMetadata(conn);
        long groupID = Long.parseLong(request.getParameter("group_id"));
        Group g = new GroupService().getGroup(groupID);
        org.json.JSONObject jo = Group.getJSON(g, conn, md);
        result.put("group", jo);
        ResponseInfo responseInfo = new ResponseInfo(200, result.toString());
        return responseInfo;
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
