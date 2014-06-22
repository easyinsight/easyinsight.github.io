package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.*;
import com.easyinsight.userupload.UserUploadService;
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
public class AccountInfoServlet extends JSONServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        setBasicAuth(false);
    }

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        SecurityUtil.authorizeAccountAdmin();
        JSONObject responseObject = new JSONObject();
        ExportMetadata md = ExportService.createExportMetadata(conn);
        AccountTransferObject uto = new UserService().retrieveAccount();
        AccountStats stats = new UserAccountAdminService().getAccountStats(conn);
        responseObject.put("account", uto.toJSON(md));
        responseObject.put("stats", stats.toJSON(md));

        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }
}
