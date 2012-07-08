package com.easyinsight.datafeeds.custom;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.AccountAdminTO;
import com.easyinsight.users.EIAccountManagementService;
import com.easyinsight.users.UserService;
import com.easyinsight.users.UserServiceResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 2/9/12
 * Time: 1:20 PM
 */
public class EIAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EIConnection conn = Database.instance().getConnection();
        try {
            String authHeader = req.getHeader("Authorization");
            if (authHeader == null) {
                resp.setContentType("application/json");
                resp.setStatus(401);
                resp.addHeader("WWW-Authenticate", "Basic realm=\"Easy Insight\"");
                resp.getOutputStream().write("Your credentials were rejected.".getBytes());
                resp.getOutputStream().flush();
                return;
            }
            String headerValue = authHeader.split(" ")[1];
            BASE64Decoder decoder = new BASE64Decoder();
            String userPass = new String(decoder.decodeBuffer(headerValue));
            int p = userPass.indexOf(":");
            UserServiceResponse userResponse = null;
            if (p != -1) {
                String userID = userPass.substring(0, p);
                String password = userPass.substring(p+1);
                try {
                    userResponse = SecurityUtil.authenticateKeys(userID, password);
                } catch (com.easyinsight.security.SecurityException se) {
                    userResponse = new UserService().authenticate(userID, password, false);
                }
            }

            if (userResponse == null || !userResponse.isSuccessful()) {
                resp.setContentType("application/json");
                resp.setStatus(401);
                resp.getOutputStream().write("Your credentials were rejected.".getBytes());
                resp.getOutputStream().flush();
            } else {
                JSONArray jsonArray = new JSONArray();
                try {
                    SecurityUtil.populateThreadLocal(userResponse.getUserName(), userResponse.getUserID(), userResponse.getAccountID(),
                            userResponse.getAccountType(), userResponse.isAccountAdmin(), userResponse.getFirstDayOfWeek(), userResponse.getPersonaName());
                    List<AccountAdminTO> accounts = new EIAccountManagementService().getAccounts();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    for (AccountAdminTO account : accounts) {
                        JSONObject obj = new JSONObject();
                        obj.put("AccountName", account.getName());
                        obj.put("AccountState", account.getAccountState());
                        obj.put("AccountType", account.getAccountType());
                        Date accountCreationDate = account.getCreationDate();
                        if (accountCreationDate == null) {
                            obj.put("CreationDate", dateFormat.format(new Date(1)));
                        } else {
                            obj.put("CreationDate", dateFormat.format(accountCreationDate));
                        }
                        Date lastLoginDate = account.getLastUserLoginDate();
                        if (lastLoginDate == null) {
                            obj.put("LastLoginDate", dateFormat.format(new Date(1)));
                        } else {
                            obj.put("LastLoginDate", dateFormat.format(lastLoginDate));
                        }
                        jsonArray.put(obj);
                    }
                    resp.setContentType("application/json");
                    resp.setStatus(200);
                    resp.getOutputStream().write(jsonArray.toString().getBytes());
                    resp.getOutputStream().flush();
                } finally {
                    SecurityUtil.clearThreadLocal();
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
