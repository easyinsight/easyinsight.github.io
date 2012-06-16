package com.easyinsight.html;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.users.Account;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 6/16/12
 * Time: 11:07 AM
 */
public class ValidBillingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String alternateDestination = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);
            PreparedStatement ps = conn.prepareStatement("SELECT account_state FROM ACCOUNT WHERE account_id = ?");
            ps.setLong(1, (Long) session.getAttribute("accountID"));
            ResultSet rs = ps.executeQuery();
            rs.next();
            int accountState = rs.getInt(1);
            if (accountState == Account.INACTIVE) {
                alternateDestination = "/app/html/reactivate.jsp";
            }
            ps.close();
        } catch (SQLException se) {
            throw new RuntimeException(se);
        } finally {
            Database.closeConnection(conn);
        }
        if (alternateDestination == null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.sendRedirect(alternateDestination);
        }
    }

    public void destroy() {

    }
}
