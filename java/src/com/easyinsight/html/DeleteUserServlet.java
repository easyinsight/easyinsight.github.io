package com.easyinsight.html;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.UserAccountAdminService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 4/24/14
 * Time: 10:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            long userID = Long.parseLong(req.getParameter("userID"));
            UserAccountAdminService service = new UserAccountAdminService();
            service.deleteUser(userID);
            resp.sendRedirect(RedirectUtil.getURL(req, "/app/html/account/users.jsp"));
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}
