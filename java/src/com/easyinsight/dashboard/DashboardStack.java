package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.preferences.ImageDescriptor;
import com.easyinsight.scorecard.Scorecard;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
    private boolean consolidateHeaderElements;
    private String selectionType;

    public String getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(String selectionType) {
        this.selectionType = selectionType;
    }

    public boolean isConsolidateHeaderElements() {
        return consolidateHeaderElements;
    }

    public void setConsolidateHeaderElements(boolean consolidateHeaderElements) {
        this.consolidateHeaderElements = consolidateHeaderElements;
    }

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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_STACK (DASHBOARD_ELEMENT_ID, STACK_SIZE, EFFECT, EFFECT_DURATION, " +
                "STACK_CONTROL, CONSOLIDATE_HEADER_ELEMENTS, SELECTION_TYPE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, getElementID());
        insertStmt.setInt(2, count);
        insertStmt.setInt(3, effectType);
        insertStmt.setInt(4, effectDuration);
        insertStmt.setInt(5, stackControl);
        insertStmt.setBoolean(6, consolidateHeaderElements);
        insertStmt.setString(7, selectionType);
        insertStmt.execute();
        long gridID = Database.instance().getAutoGenKey(insertStmt);
        int position = 0;

        for (DashboardStackItem gridItem : gridItems) {
            gridItem.setPosition(position++);
            gridItem.save(conn, gridID);
        }
        insertStmt.close();
        return id;
    }

    public static DashboardElement loadGrid(long elementID, EIConnection conn) throws SQLException {
        DashboardStack dashboardGrid = null;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD_STACK_ID, STACK_SIZE, EFFECT, EFFECT_DURATION, STACK_CONTROL, CONSOLIDATE_HEADER_ELEMENTS, SELECTION_TYPE FROM DASHBOARD_STACK WHERE DASHBOARD_ELEMENT_ID = ?");
        queryStmt.setLong(1, elementID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            dashboardGrid = new DashboardStack();
            long gridID = rs.getLong(1);
            dashboardGrid.setCount(rs.getInt(2));
            dashboardGrid.setEffectType(rs.getInt(3));
            dashboardGrid.setEffectDuration(rs.getInt(4));
            dashboardGrid.setStackControl(rs.getInt(5));
            dashboardGrid.setConsolidateHeaderElements(rs.getBoolean(6));
            dashboardGrid.setSelectionType(rs.getString(7));
            dashboardGrid.loadElement(elementID, conn);
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
            gridItemStmt.close();
            Collections.sort(items, new Comparator<DashboardStackItem>() {

                public int compare(DashboardStackItem dashboardStackItem, DashboardStackItem dashboardStackItem1) {
                    return ((Integer) dashboardStackItem.getPosition()).compareTo(dashboardStackItem1.getPosition());
                }
            });
            dashboardGrid.setGridItems(items);
            List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
            Session session = Database.instance().createSession(conn);
            try {
                PreparedStatement filterStmt = conn.prepareStatement("SELECT FILTER_ID FROM dashboard_element_to_filter where dashboard_element_id = ?");
                filterStmt.setLong(1, elementID);
                ResultSet filterRS = filterStmt.executeQuery();
                while (filterRS.next()) {
                    FilterDefinition filter = (FilterDefinition) session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterRS.getLong(1)).list().get(0);
                    filter.afterLoad();
                    filters.add(filter);
                }
                filterStmt.close();
            } finally {
                session.close();
            }
            dashboardGrid.setFilters(filters);
        }
        queryStmt.close();
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
    public void visit(IDashboardVisitor dashboardVisitor) {
        dashboardVisitor.accept(this);
        for (DashboardStackItem gridItem : gridItems) {
            if (gridItem == null || gridItem.getDashboardElement() == null) {
                continue;
            }
            gridItem.getDashboardElement().visit(dashboardVisitor);
        }
    }

    @Override
    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        for (DashboardStackItem gridItem : gridItems) {
            gridItem.getDashboardElement().updateReportIDs(reportReplacementMap);
        }
    }

    @Override
    public String toHTML() {
        StringBuilder sb = new StringBuilder();
        if (gridItems.size() > 1) {
            sb.append("<div class=\"tabbable\">");
            sb.append("<ul class=\"nav nav-pills\">");
            for (int i = 0; i < gridItems.size(); i++) {
                DashboardStackItem item = gridItems.get(i);
                if (i == 0) {
                    sb.append("<li class=\"active\">");
                } else {
                    sb.append("<li>");
                }
                String label;
                if (item.getDashboardElement() instanceof DashboardReport) {
                    label = ((DashboardReport) item.getDashboardElement()).getReport().getName();
                } else {
                    label = item.getDashboardElement().getLabel();
                }
                sb.append("<a data-toggle=\"tab\" href=\"#"+i+"\">").append(label).append("</a>");
                sb.append("</li>");
            }
            sb.append("</ul>");
            sb.append("<div class=\"tab-content\">");
            for (int i = 0; i < gridItems.size(); i++) {
                DashboardStackItem item = gridItems.get(i);
                if (i == 0) {
                    sb.append("<div class=\"tab-pane active\" id=\""+i+"\">");
                } else {
                    sb.append("<div class=\"tab-pane\" id=\""+i+"\">");
                }
                sb.append(item.getDashboardElement().toHTML());
                sb.append("</div>");
            }
            sb.append("</div>");
            sb.append("</div>");
        } else {
            sb.append(gridItems.get(0).getDashboardElement().toHTML());
        }
        return sb.toString();
    }
}
