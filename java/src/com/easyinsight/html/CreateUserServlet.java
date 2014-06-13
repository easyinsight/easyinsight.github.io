package com.easyinsight.html;

import com.easyinsight.api.v3.JSONServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.preferences.UserDLS;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.*;
import net.minidev.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 4/21/14
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateUserServlet extends JSONServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        setBasicAuth(false);
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        UserTransferObject uto = UserTransferObject.fromJSON(jsonObject);
        ExportMetadata md = ExportService.createExportMetadata(conn);
        UserAccountAdminService service = new UserAccountAdminService();
        AccountSettings settings = service.getAccountSettings();
        boolean sendEmail = settings.isSendEmail();
        if (uto.getUserID() > 0) {
            UserTransferObject newUto = null;
            long userID = uto.getUserID();
            for (UserTransferObject uu : service.getUsers()) {
                if (uu.getUserID() == userID) {
                    newUto = uu;
                }
            }
            if(newUto != null) {
                // TODO: SET THESE UP IN THE HTML INTERFACE
                uto.setCurrency(newUto.getCurrency());
                uto.setUserLocale(newUto.getUserLocale());
                uto.setDateFormat(newUto.getDateFormat());
                uto.setFixedDashboardID(newUto.getFixedDashboardID());
                uto.setPersonaID(newUto.getPersonaID());
            }
        }
        UserCreationResponse userCreationResponse;
        if (uto.getUserID() == 0) {
            userCreationResponse = service.addUserToAccount(uto, new ArrayList<UserDLS>(), Boolean.valueOf(String.valueOf(jsonObject.get("change_password"))), sendEmail);
        } else {
            userCreationResponse = service.updateUser(uto, new ArrayList<UserDLS>());
        }

        long userID = userCreationResponse.getUserID();

        for (UserTransferObject uu : service.getUsers()) {
            if (uu.getUserID() == userID) {
                uto = uu;
            }
        }

        ResponseInfo ri;
        org.json.JSONObject jo = new org.json.JSONObject();
        if(userCreationResponse.isSuccessful()) {
            jo.put("success", true);
            jo.put("user", uto.toJSON(md));
            ri = new ResponseInfo(200, jo.toString());
        } else {
            jo.put("success", false);
            ri = new ResponseInfo(500, jo.toString());
        }

        return ri;
    }
}
