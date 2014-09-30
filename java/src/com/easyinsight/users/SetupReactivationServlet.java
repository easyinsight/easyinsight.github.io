package com.easyinsight.users;

import com.easyinsight.api.v3.JSONServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: 9/16/14
 * Time: 10:38 AM
 */
public class SetupReactivationServlet extends JSONServlet {

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        long accountID = Long.parseLong(request.getParameter("id"));
        String bucket = request.getParameter("bucket");
        new ReactivationAccount().generate(accountID, conn, bucket);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
    }
}
