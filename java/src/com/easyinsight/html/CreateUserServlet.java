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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        if (uto.isConsultant()) {
            // if the user tries to set the consultant flag, don't allow that...
            SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        }
        ExportMetadata md = ExportService.createExportMetadata(conn);
        UserAccountAdminService service = new UserAccountAdminService();

        if (uto.getUserID() > 0) {
            UserTransferObject newUto = null;
            long userID = uto.getUserID();
            for (UserTransferObject uu : service.getUsers()) {
                if (uu.getUserID() == userID) {
                    newUto = uu;
                }
            }
            if(newUto != null) {

                if (!newUto.isAccountAdmin() && newUto.getUserID() == SecurityUtil.getUserID()) {
                    uto.setAnalyst(newUto.isAnalyst());
                    uto.setTestAccountVisible(newUto.isTestAccountVisible());
                    uto.setAutoRefreshReports(newUto.isAutoRefreshReports());
                    uto.setAccountAdmin(newUto.isAccountAdmin());
                    uto.setUserName(newUto.getUserName());
                }
                // TODO: SET THESE UP IN THE HTML INTERFACE
                uto.setCurrency(newUto.getCurrency());
                uto.setUserLocale(newUto.getUserLocale());
                uto.setDateFormat(newUto.getDateFormat());
                uto.setFixedDashboardID(newUto.getFixedDashboardID());
                uto.setPersonaID(newUto.getPersonaID());

            } else {
                throw new SecurityException();
            }


        }
        UserCreationResponse userCreationResponse;
        if (uto.getUserID() == 0) {
            AccountSettings settings = service.getAccountSettings();
                    boolean sendEmail = settings.isSendEmail();
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
        if(SecurityUtil.getUserID() == uto.getUserID()) {
            if(jsonObject.containsKey("new_password") && jsonObject.containsKey("old_password")) {
                String err = new UserService().updatePassword((String) jsonObject.get("old_password"), (String) jsonObject.get("new_password"));
                if(err != null) {
                    jo.put("success", false);
                    jo.put("error", err);
                }
            }

        }
        if(userCreationResponse.isSuccessful()) {
            if(!jo.has("success"))
                jo.put("success", true);
            jo.put("user", uto.toJSON(md));
            ri = new ResponseInfo(200, jo.toString());
        } else {
            jo.put("success", false);
            if(!jo.has("error")) {
                jo.put("error", userCreationResponse.getFailureMessage());
            }
            ri = new ResponseInfo(500, jo.toString());
        }

        return ri;
    }
}
