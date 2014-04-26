package com.easyinsight.html;

import com.easyinsight.preferences.UserDLS;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 4/21/14
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {

            UserAccountAdminService service = new UserAccountAdminService();
            AccountSettings settings = service.getAccountSettings();
            boolean sendEmail = settings.isSendEmail();
            boolean requirePasswordChange = "on".equals(req.getParameter("require_password_change"));
            boolean analyst = "designer".equals(req.getParameter("type"));
            UserTransferObject uto = new UserTransferObject();
            if(req.getParameter("user_id") != null) {
                long userID = Long.parseLong(req.getParameter("user_id"));
                for(UserTransferObject uu : service.getUsers()) {
                    if(uu.getUserID() == userID) {
                        uto = uu;
                    }
                }
            }

            uto.setAnalyst(analyst);
            uto.setUserName(req.getParameter("user_name"));
            uto.setFirstName(req.getParameter("first_name"));
            uto.setName(req.getParameter("last_name"));
            uto.setTestAccountVisible("on".equals(req.getParameter("all_reports_and_dashboards")));
            uto.setOptInEmail("on".equals(req.getParameter("newsletter")));
            uto.setEmail(req.getParameter("email_address"));
            uto.setAccountAdmin("on".equals(req.getParameter("account_admin")));
            uto.setInvoiceRecipient("on".equals(req.getParameter("invoice_recipient")));
            uto.setUserLocale(settings.getLocale());
            UserCreationResponse userCreationResponse;
            if(uto.getUserID() == 0) {
                userCreationResponse = service.addUserToAccount(uto, new ArrayList<UserDLS>(), requirePasswordChange, sendEmail);
            } else {
                userCreationResponse = service.updateUser(uto, new ArrayList<UserDLS>());
            }
            if(userCreationResponse.isSuccessful())
                resp.sendRedirect(RedirectUtil.getURL(req, "/app/html/account/users.jsp"));
            else
                resp.sendRedirect(RedirectUtil.getURL(req, "/app/html/account/new" + (analyst ? "Designer" : "Viewer") + "?message=" + URLEncoder.encode(userCreationResponse.getFailureMessage(), "UTF-8")));
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}
