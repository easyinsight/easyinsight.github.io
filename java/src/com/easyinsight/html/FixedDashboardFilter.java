package com.easyinsight.html;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: 5/24/12
 * Time: 9:26 AM
 */
public class FixedDashboardFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            try {
                HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);
                Long userID = (Long) session.getAttribute("userID");
                if (userID != null) {
                    EIConnection conn = Database.instance().getConnection();
                    try {
                        PreparedStatement ps = conn.prepareStatement("SELECT fixed_dashboard_id FROM USER WHERE user_id = ?");
                        ps.setLong(1, userID);
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        long fixedDashboardID = rs.getLong(1);
                        ps.close();
                        if (fixedDashboardID > 0) {
                            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                            httpServletResponse.sendRedirect(RedirectUtil.getURL((HttpServletRequest) servletRequest, "/app/fixedDashboard"));
                            return;
                        }
                    } catch (Exception e) {
                        LogClass.error(e);
                        //throw new RuntimeException(e);
                    } finally {
                        Database.closeConnection(conn);
                    }
                }
            }catch (Exception e){
                LogClass.error(e);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }
}
