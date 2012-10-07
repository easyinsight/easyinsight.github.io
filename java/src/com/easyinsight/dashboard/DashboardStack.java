package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterHTMLMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.scorecard.Scorecard;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
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
            //dashboardGrid.setCount(rs.getInt(2));
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
                if (item.getDashboardElement() != null) {
                    item.getDashboardElement().setParentElement(dashboardGrid);
                    items.add(item);
                }
            }
            dashboardGrid.setCount(items.size());
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

    public String refreshFunction() {
        if (gridItems.size() == 1) {
            return gridItems.get(0).getDashboardElement().refreshFunction();
        } else {
            String stackID = "stack" + getElementID();
            return "update" + stackID + "()";
        }
    }

    public List<FilterDefinition> filtersToRender() {

        Map<String, FilterDefinition> filterMap = new HashMap<String, FilterDefinition>();
        for (FilterDefinition filter : getFilters()) {
            filterMap.put(filter.label(false), filter);
        }
        Map<String, FilterDefinition> childFilterMap = new HashMap<String, FilterDefinition>();
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        if (consolidateHeaderElements) {
            for (DashboardStackItem item : getGridItems()) {
                Collection<? extends FilterDefinition> contained = item.getDashboardElement().filtersToRender();
                for (FilterDefinition filter : contained) {
                    if (!filterMap.containsKey(filter.label(false))) {
                        if (!childFilterMap.containsKey(filter.label(false))) {
                            childFilterMap.put(filter.label(false), filter);
                            countMap.put(filter.label(false), 1);
                        } else {
                            countMap.put(filter.label(false), countMap.get(filter.label(false)) + 1);
                        }
                    }
                }
            }
        }
        for (Map.Entry<String, FilterDefinition> entry : childFilterMap.entrySet()) {
            int count = countMap.get(entry.getKey());
            if (count == getGridItems().size()) {
                filterMap.put(entry.getKey(), entry.getValue());
            }
        }

        return new ArrayList<FilterDefinition>(filterMap.values());
    }

    private boolean parentConsolidatesFilters(DashboardElement parent) {
        if (parent == null) {
            return false;
        }
        if (parent instanceof DashboardStack) {
            DashboardStack dashboardStack = (DashboardStack) parent;
            if (dashboardStack.consolidateHeaderElements) {
                return true;
            }
        }
        return parentConsolidatesFilters(parent.getParentElement());
    }

    @Override
    public String toHTML(FilterHTMLMetadata filterHTMLMetadata) {
        StringBuilder sb = new StringBuilder();
        String stackID = "stack" + getElementID();

        List<FilterDefinition> filters = filtersToRender();
        if (getParentElement() != null) {
            Map<String, FilterDefinition> filterMap = new HashMap<String, FilterDefinition>();
            for (FilterDefinition filter : filters) {
                filterMap.put(filter.label(false), filter);
            }
            Collection<? extends FilterDefinition> parentFilters = getParentElement().filtersToRender();

            for (FilterDefinition filter : parentFilters) {
                filterMap.remove(filter.label(false));
            }
            filters = new ArrayList<FilterDefinition>(filterMap.values());
        }
        for (FilterDefinition filter : filters) {
            if (filter.isShowOnReportView()) {
                FilterHTMLMetadata metadata = new FilterHTMLMetadata(filterHTMLMetadata.getDashboard(), filterHTMLMetadata.getRequest(), null, false);
                if (gridItems.size() > 1) {
                    metadata.setOnChange("update" + stackID + "");
                } else {
                    String refreshFunction = gridItems.get(0).getDashboardElement().refreshFunction();
                    refreshFunction = refreshFunction.substring(0, refreshFunction.length() - 2);
                    metadata.setOnChange(refreshFunction);
                }
                metadata.setFilterKey(stackID);
                sb.append("<div class=\"filterDiv\">").append(filter.toHTML(metadata)).append("</div>");
            }
        }

        if (gridItems.size() > 1) {
            if ("Buttons".equals(selectionType)) {
                sb.append("<div class=\"tabbable\">");
                sb.append("<ul style=\"text-align:right\" class=\"nav nav-pills\" id=\"").append(stackID).append("\">");
                for (int i = 0; i < gridItems.size(); i++) {
                    DashboardStackItem item = gridItems.get(i);
                    if (i == 0) {
                        sb.append("<li style=\"float:none;display:inline-block;\" class=\"active\">");
                    } else {
                        sb.append("<li style=\"float:none;display:inline-block;\">");
                    }
                    String label;
                    if (item.getDashboardElement() instanceof DashboardReport) {
                        label = ((DashboardReport) item.getDashboardElement()).getReport().getName();
                    } else {
                        label = item.getDashboardElement().getLabel();
                    }
                    sb.append("<a class=\"argh\" data-toggle=\"tab\" href=\"#ds").append(item.getDashboardElement().getElementID()).append("\">").append(label).append("</a>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("<div class=\"tab-content\">");
                for (int i = 0; i < gridItems.size(); i++) {
                    DashboardStackItem item = gridItems.get(i);
                    if (i == 0) {
                        sb.append("<div class=\"tab-pane active\"");
                    } else {
                        sb.append("<div class=\"tab-pane\"");
                    }
                    sb.append(" id=\"").append("ds").append(item.getDashboardElement().getElementID()).append("\"></div>");
                }
                sb.append("</div>");
                sb.append("</div>");
                String globalVar = "var state" + stackID + " = 'ds" + gridItems.get(0).getDashboardElement().getElementID() + "';\n";
                String update = "function update" + stackID + "() {\n";
                for (int i = 0; i < gridItems.size(); i++) {
                    DashboardStackItem item = gridItems.get(i);
                    if (i == 0) {
                        update += "if (state" + stackID + " == 'ds" + item.getDashboardElement().getElementID() + "') {\n";
                    } else {
                        update += "else if (state" + stackID + " == 'ds" + item.getDashboardElement().getElementID() + "') {\n";
                    }
                    update += item.getDashboardElement().refreshFunction()+"\n";
                    update += "}\n";
                }
                update += "}";

                sb.append("\n<script type=\"text/javascript\">\n");
                sb.append(globalVar);
                sb.append(update);
                String initString;
                String pieceString;
                if (filterHTMLMetadata.getDrillthroughKey() == null) {
                    initString = "/app/dashboardPiece?dashboardElementID="+gridItems.get(0).getDashboardElement().getElementID()+"&embedded="+filterHTMLMetadata.isEmbedded()+"&dashboardID="+filterHTMLMetadata.getDashboard().getId();
                    pieceString = "/app/dashboardPiece?dashboardElementID=' + contentID.replace('#', '')+'&dashboardID="+filterHTMLMetadata.getDashboard().getId()+"&embedded="+filterHTMLMetadata.isEmbedded();
                } else {
                    initString = "/app/dashboardPiece?dashboardElementID="+gridItems.get(0).getDashboardElement().getElementID()+"&embedded="+filterHTMLMetadata.isEmbedded()+"&dashboardID="+filterHTMLMetadata.getDashboard().getId()+"&drillThroughKey="+filterHTMLMetadata.getDrillthroughKey();
                    pieceString = "/app/dashboardPiece?dashboardElementID=' + contentID.replace('#', '')+'&dashboardID="+filterHTMLMetadata.getDashboard().getId()+"&embedded="+filterHTMLMetadata.isEmbedded()+"&drillThroughKey="+filterHTMLMetadata.getDrillthroughKey();
                }
                sb.append("    $(function() {\n" +
                        "        $('#ds"+gridItems.get(0).getDashboardElement().getElementID()+"').load('"+initString+"', function() {\n" +
                        "            $('#"+stackID+"').tab(); //initialize tabs\n" +
                        "        });    \n" +
                        "        $('#"+stackID+"').bind('show', function(e) {    \n" +
                        "           var pattern=/#.+/gi //use regex to get anchor(==selector)\n" +
                        "           var contentID = e.target.toString().match(pattern)[0]; //get anchor         \n" +
                        "            $(contentID).load('"+pieceString+"', function(){\n" +
                        "                $('#"+stackID+"').tab(); //reinitialize tabs\n" +
                        "state" + stackID + " = contentID.replace('#', '');\n" +
                        "            });\n" +
                        "        });\n" +
                        "    });\n" +
                        "</script>");
            } else {
                String selectID = "dashboardSelect" + getElementID();
                String divID = "divSelectTarget" + getElementID();
                String elementIDString = String.valueOf(getElementID());
                sb.append("\n<script type=\"text/javascript\">\n");
                sb.append(MessageFormat.format("function updateStackDropdown{0}() '{'\n", elementIDString));
                sb.append(MessageFormat.format("var optionMenu = document.getElementById(\"{0}\");\n", selectID));
                sb.append("var chosenOption = optionMenu.options[optionMenu.selectedIndex];\n");
                if (filterHTMLMetadata.getDrillthroughKey() == null) {
                    sb.append("$.get('/app/dashboardPiece?dashboardElementID='+chosenOption.value+'&embedded="+filterHTMLMetadata.isEmbedded()+"&dashboardID=" + filterHTMLMetadata.getDashboard().getId()+"', function(data){\n");
                } else {
                    sb.append("$.get('/app/dashboardPiece?dashboardElementID='+chosenOption.value+'&embedded="+filterHTMLMetadata.isEmbedded()+"&dashboardID=" + filterHTMLMetadata.getDashboard().getId()+"&drillThroughKey="+filterHTMLMetadata.getDrillthroughKey()+"', function(data){\n");
                }
                sb.append("$('#" + divID + "').html(data);\n");
                sb.append("});\n");
                sb.append("}\n");
                sb.append("function update").append(stackID).append("() {\n");
                sb.append(MessageFormat.format("var optionMenu = document.getElementById(\"{0}\");\n", selectID));
                sb.append("var chosenOption = optionMenu.options[optionMenu.selectedIndex];\n");
                for (int i = 0; i < gridItems.size(); i++) {
                    DashboardStackItem item = gridItems.get(i);
                    if (i == 0) {
                        sb.append("if (chosenOption.value == '" + item.getDashboardElement().getElementID() + "') {\n");
                    } else {
                        sb.append("else if (chosenOption.value == '" + item.getDashboardElement().getElementID() + "') {\n");
                    }
                    sb.append(item.getDashboardElement().refreshFunction()+"\n");
                    sb.append("}\n");
                }
                sb.append("}\n");
                sb.append("</script>\n");
                String onChange = "updateStackDropdown"+getElementID()+"()";
                sb.append(MessageFormat.format("<select id=\"{0}\" onChange=\"{1}\">", selectID, onChange));
                for (int i = 0; i < gridItems.size(); i++) {
                    DashboardStackItem item = gridItems.get(i);
                    String label;
                    if (item.getDashboardElement() instanceof DashboardReport) {
                        label = ((DashboardReport) item.getDashboardElement()).getReport().getName();
                    } else {
                        label = item.getDashboardElement().getLabel();
                    }
                    sb.append("<option value=\"").append(gridItems.get(i).getDashboardElement().getElementID()).append("\">").append(label).append("</option>");
                }
                sb.append("</select>");
                sb.append(MessageFormat.format("<div style=\"float:left;clear:left;width:100%;height:100%\" id=\"{0}\">", divID));
                sb.append(gridItems.get(0).getDashboardElement().toHTML(filterHTMLMetadata));
                sb.append("</div>");
                System.out.println(sb.toString());
            }
        } else {
            sb.append("<div style=\"float:left;clear:left;width:100%;height:100%\">");
            sb.append(gridItems.get(0).getDashboardElement().toHTML(filterHTMLMetadata));
            sb.append("</div>");
        }
        //sb.append("</div>");
        return sb.toString();
    }

    public List<String> jsIncludes() {
        List<String> includes = super.jsIncludes();
        for (DashboardStackItem stackItem : getGridItems()) {
            includes.addAll(stackItem.getDashboardElement().jsIncludes());
        }
        return includes;
    }

    public List<String> cssIncludes() {
        List<String> includes = super.cssIncludes();
        for (DashboardStackItem stackItem : getGridItems()) {
            includes.addAll(stackItem.getDashboardElement().cssIncludes());
        }
        return includes;
    }

    @Override
    public Collection<? extends FilterDefinition> filtersForReport(long reportID) {
        for (DashboardStackItem stackItem : getGridItems()) {
            Collection<? extends FilterDefinition> filters = stackItem.getDashboardElement().filtersForReport(reportID);
            if (filters != null && !filters.isEmpty()) {
                return filters;
            }
        }
        return new ArrayList<FilterDefinition>();
    }

    @Override
    public void populateFilters(List<FilterDefinition> parentFilters) {
        if (getFilters() != null) {
            parentFilters.addAll(getFilters());
        }
        super.populateFilters(parentFilters);
    }

    public DashboardUIProperties findHeaderImage() {
        if (getHeaderBackground() != null) {
            return new DashboardUIProperties(getHeaderBackgroundColor(), getHeaderBackground());
        }
        for (DashboardStackItem stackItem : getGridItems()) {
            DashboardUIProperties imageDescriptor = stackItem.getDashboardElement().findHeaderImage();
            if (imageDescriptor != null) {
                return imageDescriptor;
            }
        }
        return null;
    }

    public DashboardElement findElement(long dashboardElementID) {
        DashboardElement element = super.findElement(dashboardElementID);
        if (element != null) {
            return element;
        }
        for (DashboardStackItem stackItem : getGridItems()) {
            element = stackItem.getDashboardElement().findElement(dashboardElementID);
            if (element != null) {
                return element;
            }
        }
        return null;
    }

    public int requiredInitCount() {
        DashboardStackItem dashboardStackItem = getGridItems().get(0);
        return dashboardStackItem.getDashboardElement().requiredInitCount();
    }
}
