package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.PreferencesService;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.AccountStats;
import com.easyinsight.users.AccountTransferObject;
import com.easyinsight.users.UserAccountAdminService;
import com.easyinsight.users.UserService;
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
public class AccountSkinServlet extends JSONServlet {

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

        ApplicationSkin as = new PreferencesService().getAccountSkin();
        responseObject.put("skin", as.toJSON(md));


        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

    @Override
    protected ResponseInfo processPost(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        SecurityUtil.authorizeAccountAdmin();

        JSONObject responseObject = new JSONObject();
        ApplicationSkin skin = ApplicationSkin.fromJSON(jsonObject);
        new PreferencesService().saveAccountSkin(skin);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());

    }

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
