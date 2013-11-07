package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.ApplicationSkinSettings;
import com.easyinsight.preferences.ImageDescriptor;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 10/3/12
 * Time: 3:59 PM
 */
public class Utils {

    public static boolean isPublic(long filterID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            Long reportID = null;
            Long dashboardID = null;
            Long dashboardElementID = null;
            FilterDefinition filter;
            Session session = Database.instance().createSession(conn);
            try {
                filter = (FilterDefinition) session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list().get(0);
                filter.afterLoad();
            } finally {
                session.close();
            }
            PreparedStatement ps = conn.prepareStatement("SELECT ANALYSIS_ID FROM analysis_to_filter_join WHERE filter_id = ?");
            ps.setLong(1, filterID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                reportID = rs.getLong(1);
            } else {
                PreparedStatement dashboardPS = conn.prepareStatement("SELECT dashboard_id FROM dashboard_to_filter WHERE filter_id = ?");
                dashboardPS.setLong(1, filterID);
                ResultSet dashboardRS = dashboardPS.executeQuery();
                if (dashboardRS.next()) {
                    dashboardID = dashboardRS.getLong(1);
                } else {
                    PreparedStatement dashboardElementPS = conn.prepareStatement("SELECT dashboard_element.dashboard_element_id from " +
                            "dashboard_element_to_filter, dashboard_element WHERE filter_id = ?");
                    dashboardElementPS.setLong(1, filterID);
                    ResultSet dashboardElementRS = dashboardElementPS.executeQuery();
                    while (dashboardElementRS.next()) {
                        dashboardElementID = dashboardElementRS.getLong(1);
                    }
                    dashboardElementPS.close();
                }
                dashboardPS.close();
            }
            ps.close();
            boolean publicVisibility;
            if (reportID != null) {
                WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID);
                publicVisibility = report.isPubliclyVisible();
            } else if (dashboardID != null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT public_visible FROM dashboard WHERE dashboard_id = ?");
                stmt.setLong(1, dashboardID);
                ResultSet dashboardRS = stmt.executeQuery();
                dashboardRS.next();
                publicVisibility = dashboardRS.getBoolean(1);
                stmt.close();
            } else if (dashboardElementID != null) {
                PreparedStatement rootStmt = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_ID, DATA_SOURCE_ID, public_visible FROM DASHBOARD, DASHBOARD_TO_DASHBOARD_ELEMENT WHERE DASHBOARD_ELEMENT_ID = ? AND " +
                        "DASHBOARD_TO_DASHBOARD_ELEMENT.DASHBOARD_ID = DASHBOARD.DASHBOARD_ID");
                PreparedStatement findParentInGridStmt = conn.prepareStatement("SELECT DASHBOARD_GRID.DASHBOARD_ELEMENT_ID  FROM " +
                        "DASHBOARD_GRID, DASHBOARD_GRID_ITEM WHERE DASHBOARD_GRID_ITEM.DASHBOARD_ELEMENT_ID = ? AND DASHBOARD_GRID_ITEM.DASHBOARD_GRID_ID = DASHBOARD_GRID.DASHBOARD_GRID_ID");
                PreparedStatement findParentInStackStmt = conn.prepareStatement("SELECT DASHBOARD_STACK.DASHBOARD_ELEMENT_ID  FROM " +
                        "DASHBOARD_STACK, DASHBOARD_STACK_ITEM WHERE DASHBOARD_STACK_ITEM.DASHBOARD_ELEMENT_ID = ? AND DASHBOARD_STACK_ITEM.DASHBOARD_STACK_ID = DASHBOARD_STACK.DASHBOARD_STACK_ID");
                Blah blah = findDashboard(dashboardElementID, rootStmt, findParentInGridStmt, findParentInStackStmt);
                if (blah == null) {
                    throw new RuntimeException();
                }
                publicVisibility = blah.publiclyVisible;
                rootStmt.close();
                findParentInGridStmt.close();
                findParentInStackStmt.close();
            } else {
                throw new RuntimeException();
            }
            return publicVisibility;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private static Blah findDashboard(long dashboardElementID, PreparedStatement rootStmt, PreparedStatement findParentInGridStmt, PreparedStatement findParentInStackStmt) throws SQLException {
        rootStmt.setLong(1, dashboardElementID);
        ResultSet rootRS = rootStmt.executeQuery();
        if (rootRS.next()) {
            return new Blah(rootRS.getLong(1), rootRS.getLong(2), rootRS.getBoolean(3));
        }
        findParentInGridStmt.setLong(1, dashboardElementID);
        ResultSet gridRS = findParentInGridStmt.executeQuery();
        if (gridRS.next()) {
            return findDashboard(gridRS.getLong(1), rootStmt, findParentInGridStmt, findParentInStackStmt);
        }
        findParentInStackStmt.setLong(1, dashboardElementID);
        ResultSet stackRS = findParentInStackStmt.executeQuery();
        if (stackRS.next()) {
            return findDashboard(stackRS.getLong(1), rootStmt, findParentInGridStmt, findParentInStackStmt);
        }
        return null;
    }

    private static class Blah {
        long dashboardID;
        long dataSourceID;
        boolean publiclyVisible;

        private Blah(long dashboardID, long dataSourceID, boolean publiclyVisible) {
            this.dashboardID = dashboardID;
            this.dataSourceID = dataSourceID;
            this.publiclyVisible = publiclyVisible;
        }
    }

    public static boolean isPhone(HttpServletRequest request) {
        return request.getHeader("User-Agent").toLowerCase().matches(".*(android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||request.getHeader("User-Agent").toLowerCase().substring(0,4).matches("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-");
    }

    public static boolean isDesigner() {
        EIConnection conn = Database.instance().getConnection();
        boolean designer;
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYST FROM USER WHERE USER_ID = ?");
            queryStmt.setLong(1, SecurityUtil.getUserID(false));
            ResultSet rs = queryStmt.executeQuery();
            if(!rs.next()) return false;
            designer = rs.getBoolean(1);
            queryStmt.close();
            return designer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static boolean isTablet(HttpServletRequest request) {
        return request.getHeader("User-Agent").toLowerCase().contains("ipad");
    }

    public static UIData createUIData() {
        Session hibernateSession = Database.instance().createSession();
        ApplicationSkin applicationSkin = null;
        String headerStyle = "";
        try {
            long userID = SecurityUtil.getUserID(false);
            if (userID > 0) {
                applicationSkin = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), hibernateSession, SecurityUtil.getAccountID());
                headerStyle = "width:100%;overflow: hidden;";
            }
        } finally {
            hibernateSession.close();
        }
        ImageDescriptor headerImageDescriptor = null;
        String headerTextStyle = "width: 100%;text-align: center;font-size: 18px;padding-top:15px;font-weight:bold;";
        if (applicationSkin != null && applicationSkin.isReportHeader()) {
            headerImageDescriptor = applicationSkin.getReportHeaderImage();
            int reportBackgroundColor = applicationSkin.getReportBackgroundColor();
            headerStyle += "background-color: " + String.format("#%06X", (0xFFFFFF & reportBackgroundColor));
            headerTextStyle += "color: " + String.format("#%06X", (0xFFFFFF & applicationSkin.getReportTextColor()));
        }
        return new UIData(applicationSkin, headerStyle, headerTextStyle, headerImageDescriptor);
    }

    public static DrillThroughData drillThroughFiltersForReport(String drillthroughArgh) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            List<FilterDefinition> drillthroughFilters = new ArrayList<FilterDefinition>();
            PreparedStatement queryStmt = conn.prepareStatement("SELECT report_id, drillthrough_save_id FROM drillthrough_save WHERE url_key = ?");
            queryStmt.setString(1, drillthroughArgh);
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            long reportID = rs.getLong(1);
            long drillthroughSaveID = rs.getLong(2);
            PreparedStatement filterStmt = conn.prepareStatement("SELECT filter_id from drillthrough_report_save_filter WHERE drillthrough_save_id = ?");
            filterStmt.setLong(1, drillthroughSaveID);
            ResultSet filterRS = filterStmt.executeQuery();
            while (filterRS.next()) {
                Session hibernateSession = Database.instance().createSession(conn);
                FilterDefinition filter = (FilterDefinition) hibernateSession.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterRS.getLong(1)).list().get(0);
                filter.afterLoad();
                drillthroughFilters.add(filter);
                hibernateSession.close();
            }
            return new DrillThroughData(reportID, 0, drillthroughFilters);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static DrillThroughData drillThroughFiltersForDashboard(String drillthroughArgh) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            List<FilterDefinition> drillthroughFilters = new ArrayList<FilterDefinition>();
            PreparedStatement queryStmt = conn.prepareStatement("SELECT dashboard_id, drillthrough_save_id FROM drillthrough_save WHERE url_key = ?");
            queryStmt.setString(1, drillthroughArgh);
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            long dashboardID = rs.getLong(1);
            long drillthroughSaveID = rs.getLong(2);
            PreparedStatement filterStmt = conn.prepareStatement("SELECT filter_id from drillthrough_report_save_filter WHERE drillthrough_save_id = ?");
            filterStmt.setLong(1, drillthroughSaveID);
            ResultSet filterRS = filterStmt.executeQuery();
            while (filterRS.next()) {
                Session hibernateSession = Database.instance().createSession(conn);
                FilterDefinition filter = (FilterDefinition) hibernateSession.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterRS.getLong(1)).list().get(0);
                filter.afterLoad();
                drillthroughFilters.add(filter);
                hibernateSession.close();
            }
            return new DrillThroughData(0, dashboardID, drillthroughFilters);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
