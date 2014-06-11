package com.easyinsight.api.v3;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.User;
import com.easyinsight.users.UserService;
import com.easyinsight.userupload.UserUploadService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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

        responseObject.put("user", u.toUserTransferObject().toJSON(md));

        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }
}
