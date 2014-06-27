package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.*;
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
public class AccountSettingsServlet extends JSONServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        setBasicAuth(false);
    }

    @Override
    protected ResponseInfo processGet(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        SecurityUtil.authorizeAccountAdmin();
        JSONObject responseObject = new JSONObject();
        ExportMetadata md = ExportService.createExportMetadata(conn);
        AccountSettings settings = new UserAccountAdminService().getAccountSettings();
        responseObject.put("settings", settings.toJSON(md));

        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected ResponseInfo processPost(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        SecurityUtil.authorizeAccountAdmin();
        JSONObject responseObject = new JSONObject();
        ExportMetadata md = ExportService.createExportMetadata(conn);
        AccountSettings as = AccountSettings.fromJSON(jsonObject);
        new UserAccountAdminService().saveAccountSettings(as);
        responseObject.put("success", true);
        responseObject.put("settings", as.toJSON(md));

        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }
}
