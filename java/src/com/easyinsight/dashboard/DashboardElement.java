package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:25:06 PM
 */
public abstract class DashboardElement implements Cloneable {

    public static final int GRID = 1;
    public static final int REPORT = 2;
    public static final int STACK = 3;
    public static final int IMAGE = 4;

    private long elementID;

    public DashboardElement clone() throws CloneNotSupportedException {
        DashboardElement dashboardElement = (DashboardElement) super.clone();
        dashboardElement.setElementID(0);
        return dashboardElement;
    }

    public long getElementID() {
        return elementID;
    }

    public void setElementID(long elementID) {
        this.elementID = elementID;
    }

    public abstract int getType();

    public long save(EIConnection conn) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_ELEMENT (ELEMENT_TYPE) VALUES (?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setInt(1, getType());
        insertStmt.execute();
        setElementID(Database.instance().getAutoGenKey(insertStmt));
        insertStmt.close();
        return elementID;
    }

    public abstract Set<Long> containedReports();
    public abstract void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap);
}
