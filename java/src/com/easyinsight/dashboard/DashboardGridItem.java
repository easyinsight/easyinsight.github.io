package com.easyinsight.dashboard;

import com.easyinsight.database.EIConnection;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 2:01:39 PM
 */
public class DashboardGridItem implements Cloneable, Serializable {
    private DashboardElement dashboardElement;
    private int columnIndex;
    private int rowIndex;

    public DashboardGridItem clone() throws CloneNotSupportedException {
        DashboardGridItem gridItem = (DashboardGridItem) super.clone();
        gridItem.setDashboardElement(dashboardElement.clone());
        return gridItem;
    }

    @Nullable
    public DashboardElement getDashboardElement() {
        return dashboardElement;
    }

    public void setDashboardElement(DashboardElement dashboardElement) {
        this.dashboardElement = dashboardElement;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void save(EIConnection conn, long dashboardGridID) throws SQLException {
        dashboardElement.save(conn);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_GRID_ITEM (COLUMN_POSITION, ROW_POSITION," +
                "DASHBOARD_ELEMENT_ID, DASHBOARD_GRID_ID) VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setInt(1, columnIndex);
        insertStmt.setInt(2, rowIndex);
        insertStmt.setLong(3, dashboardElement.getElementID());
        insertStmt.setLong(4, dashboardGridID);
        insertStmt.execute();
        insertStmt.close();
    }
}
