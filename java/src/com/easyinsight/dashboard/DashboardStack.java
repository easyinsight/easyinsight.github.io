package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.scorecard.Scorecard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:25:12 PM
 */
public class DashboardStack extends DashboardElement {

    private List<DashboardStackItem> gridItems;
    private int count;
    private int effectDuration;
    private int effectType;
    private int stackControl;

    public int getStackControl() {
        return stackControl;
    }

    public void setStackControl(int stackControl) {
        this.stackControl = stackControl;
    }

    public int getEffectDuration() {
        return effectDuration;
    }

    public void setEffectDuration(int effectDuration) {
        this.effectDuration = effectDuration;
    }

    public int getEffectType() {
        return effectType;
    }

    public void setEffectType(int effectType) {
        this.effectType = effectType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public DashboardElement clone() throws CloneNotSupportedException {
        DashboardStack dashboardGrid = (DashboardStack) super.clone();
        List<DashboardStackItem> cloneItems = new ArrayList<DashboardStackItem>();
        for (DashboardStackItem gridItem : gridItems) {
            cloneItems.add(gridItem.clone());
        }
        dashboardGrid.setGridItems(cloneItems);
        return dashboardGrid;
    }

    public List<DashboardStackItem> getGridItems() {
        return gridItems;
    }

    public void setGridItems(List<DashboardStackItem> gridItems) {
        this.gridItems = gridItems;
    }

    @Override
    public int getType() {
        return DashboardElement.STACK;
    }

    @Override
    public long save(EIConnection conn) throws SQLException {
        long id = super.save(conn);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_STACK (DASHBOARD_ELEMENT_ID, STACK_SIZE, EFFECT, EFFECT_DURATION, STACK_CONTROL) VALUES (?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, getElementID());
        insertStmt.setInt(2, count);
        insertStmt.setInt(3, effectType);
        insertStmt.setInt(4, effectDuration);
        insertStmt.setInt(5, stackControl);
        insertStmt.execute();
        long gridID = Database.instance().getAutoGenKey(insertStmt);
        for (DashboardStackItem gridItem : gridItems) {
            gridItem.save(conn, gridID);
        }
        return id;
    }

    public static DashboardElement loadGrid(long elementID, EIConnection conn) throws SQLException {
        DashboardStack dashboardGrid = null;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD_STACK_ID, STACK_SIZE, EFFECT, EFFECT_DURATION, STACK_CONTROL FROM DASHBOARD_STACK WHERE DASHBOARD_ELEMENT_ID = ?");
        queryStmt.setLong(1, elementID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            dashboardGrid = new DashboardStack();
            long gridID = rs.getLong(1);
            dashboardGrid.setCount(rs.getInt(2));
            dashboardGrid.setEffectType(rs.getInt(3));
            dashboardGrid.setEffectDuration(rs.getInt(4));
            dashboardGrid.setStackControl(rs.getInt(5));
            PreparedStatement gridItemStmt = conn.prepareStatement("SELECT DASHBOARD_ELEMENT.DASHBOARD_ELEMENT_ID, DASHBOARD_ELEMENT.element_type, " +
                    "ITEM_POSITION FROM DASHBOARD_STACK_ITEM, DASHBOARD_ELEMENT WHERE DASHBOARD_STACK_ID = ? AND DASHBOARD_STACK_ITEM.dashboard_element_id = dashboard_element.dashboard_element_id");
            gridItemStmt.setLong(1, gridID);
            ResultSet itemRS = gridItemStmt.executeQuery();
            List<DashboardStackItem> items = new ArrayList<DashboardStackItem>();
            while (itemRS.next()) {
                long gridElementID = itemRS.getLong(1);
                int elementType = itemRS.getInt(2);
                DashboardStackItem item = new DashboardStackItem();
                item.setPosition(itemRS.getInt(3));
                item.setDashboardElement(DashboardStorage.getElement(conn, gridElementID, elementType));
                items.add(item);
            }
            dashboardGrid.setGridItems(items);
        }
        return dashboardGrid;
    }

    @Override
    public Set<Long> containedReports() {
        Set<Long> reports = new HashSet<Long>();
        for (DashboardStackItem gridItem : gridItems) {
            reports.addAll(gridItem.getDashboardElement().containedReports());
        }
        return reports;
    }

    @Override
    public Set<Long> containedScorecards() {
        Set<Long> reports = new HashSet<Long>();
        for (DashboardStackItem gridItem : gridItems) {
            reports.addAll(gridItem.getDashboardElement().containedScorecards());
        }
        return reports;
    }

    @Override
    public void updateScorecardIDs(Map<Long, Scorecard> scorecardReplacementMap) {
        for (DashboardStackItem gridItem : gridItems) {
            gridItem.getDashboardElement().updateScorecardIDs(scorecardReplacementMap);
        }
    }

    @Override
    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        for (DashboardStackItem gridItem : gridItems) {
            gridItem.getDashboardElement().updateReportIDs(reportReplacementMap);
        }
    }
}
