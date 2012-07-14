package com.easyinsight.dashboard;

import com.easyinsight.database.EIConnection;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 2:01:39 PM
 */
public class DashboardStackItem implements Cloneable, Serializable {
    private DashboardElement dashboardElement;
    private int position;

    public DashboardStackItem clone() throws CloneNotSupportedException {
        DashboardStackItem gridItem = (DashboardStackItem) super.clone();
        gridItem.setDashboardElement(dashboardElement.clone());
        return gridItem;
    }

    public DashboardElement getDashboardElement() {
        return dashboardElement;
    }

    public void setDashboardElement(DashboardElement dashboardElement) {
        this.dashboardElement = dashboardElement;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void save(EIConnection conn, long dashboardGridID) throws SQLException {
        dashboardElement.save(conn);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_STACK_ITEM (item_position," +
                "DASHBOARD_ELEMENT_ID, DASHBOARD_STACK_ID) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setInt(1, position);
        insertStmt.setLong(2, dashboardElement.getElementID());
        insertStmt.setLong(3, dashboardGridID);
        insertStmt.execute();
    }
}
