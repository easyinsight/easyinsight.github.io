package com.easyinsight.dashboard;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.logging.LogClass;
import com.easyinsight.scorecard.Scorecard;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private boolean consolidateHeaderElements;
    private String selectionType = "Buttons";
    private int stackFontSize = 16;
    private int defaultIndex;


    public int getDefaultIndex() {
        return defaultIndex;
    }

    public void setDefaultIndex(int defaultIndex) {
        this.defaultIndex = defaultIndex;
    }

    public int getStackFontSize() {
        return stackFontSize;
    }

    public void setStackFontSize(int stackFontSize) {
        this.stackFontSize = stackFontSize;
    }

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
                "STACK_CONTROL, CONSOLIDATE_HEADER_ELEMENTS, SELECTION_TYPE, STACK_FONT_SIZE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, getElementID());
        insertStmt.setInt(2, count);
        insertStmt.setInt(3, effectType);
        insertStmt.setInt(4, effectDuration);
        insertStmt.setInt(5, stackControl);
        insertStmt.setBoolean(6, consolidateHeaderElements);
        insertStmt.setString(7, selectionType);
        insertStmt.setInt(8, stackFontSize);
        insertStmt.execute();
        long gridID = Database.instance().getAutoGenKey(insertStmt);
        int position = 0;

        insertStmt.close();

        for (DashboardStackItem gridItem : gridItems) {
            gridItem.setPosition(position++);
            gridItem.save(conn, gridID);
        }

        return id;
    }

    public static DashboardElement loadGrid(long elementID, EIConnection conn) throws SQLException {
        DashboardStack dashboardGrid = null;
        long gridID = 0;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD_STACK_ID, STACK_SIZE, EFFECT, EFFECT_DURATION, STACK_CONTROL, CONSOLIDATE_HEADER_ELEMENTS, SELECTION_TYPE, STACK_FONT_SIZE FROM DASHBOARD_STACK WHERE DASHBOARD_ELEMENT_ID = ?");
        queryStmt.setLong(1, elementID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            dashboardGrid = new DashboardStack();

            gridID = rs.getLong(1);
            //dashboardGrid.setCount(rs.getInt(2));
            dashboardGrid.setEffectType(rs.getInt(3));
            dashboardGrid.setEffectDuration(rs.getInt(4));
            dashboardGrid.setStackControl(rs.getInt(5));
            dashboardGrid.setConsolidateHeaderElements(rs.getBoolean(6));
            dashboardGrid.setSelectionType(rs.getString(7));
            dashboardGrid.setStackFontSize(rs.getInt(8));
            dashboardGrid.loadElement(elementID, conn);
        } else {
        }
        queryStmt.close();
        if (dashboardGrid != null) {
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
                PreparedStatement filterStmt = conn.prepareStatement("SELECT FILTER_ID FROM dashboard_element_to_filter WHERE dashboard_element_id = ?");
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
            PreparedStatement filterSetStmt = conn.prepareStatement("SELECT filter_set.filter_set_id, filter_set.filter_set_name FROM dashboard_element_to_filter_set, filter_set " +
                    "WHERE dashboard_element_id = ? AND dashboard_element_to_filter_set.filter_set_id = filter_set.filter_set_id order by dashboard_element_to_filter_set.position_index");
            filterSetStmt.setLong(1, elementID);
            ResultSet filterSetRS = filterSetStmt.executeQuery();
            List<FilterSetDescriptor> filterSetDescriptors = new ArrayList<FilterSetDescriptor>();
            while (filterSetRS.next()) {
                long filterSetID = filterSetRS.getLong(1);
                String name = filterSetRS.getString(2);
                FilterSetDescriptor filterSetDescriptor = new FilterSetDescriptor();
                filterSetDescriptor.setName(name);
                filterSetDescriptor.setId(filterSetID);
                filterSetDescriptors.add(filterSetDescriptor);
            }
            filterSetStmt.close();

            dashboardGrid.setFilterSets(filterSetDescriptors);

            try {
                for (FilterSetDescriptor filterSetDescriptor : filterSetDescriptors) {
                    FilterSet filterSet = loadFilterSet(filterSetDescriptor, conn);
                    for (FilterDefinition filterDefinition : filterSet.getFilters()) {
                        FilterDefinition clone = filterDefinition.clone();
                        clone.setFromFilterSet(filterSetDescriptor.getId());
                        filters.add(clone);
                    }
                }
            } catch (Exception e) {
                LogClass.error(e);
            }
        }
        return dashboardGrid;
    }

    private static FilterSet loadFilterSet(FilterSetDescriptor filterSetDescriptor, EIConnection conn) {
        return new FilterSetStorage().getFilterSet(filterSetDescriptor.getId(), conn);
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

    private void cleanup(AnalysisItem analysisItem, boolean changingDataSource) {
        if (changingDataSource) {
            // TODO: validate calculations and lookup tables--if necessary to create, should emit something with the report
            analysisItem.setLookupTableID(null);
            analysisItem.setBasedOnReportField(null);
        }
    }

    @Override
    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap, List<AnalysisItem> allFields, boolean changingDataSource, FeedDefinition dataSource) {
        for (DashboardStackItem gridItem : gridItems) {
            gridItem.getDashboardElement().updateReportIDs(reportReplacementMap, allFields, changingDataSource, dataSource);
        }

        try {
            if (getFilters() != null) {
                Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
                List<FilterDefinition> filterDefinitions = new ArrayList<FilterDefinition>();
                for (FilterDefinition persistableFilterDefinition : getFilters()) {
                    filterDefinitions.add(persistableFilterDefinition.clone());
                    List<AnalysisItem> filterItems = persistableFilterDefinition.getAnalysisItems(allFields, new ArrayList<AnalysisItem>(), true, true, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(null));
                    for (AnalysisItem item : filterItems) {
                        if (replacementMap.get(item.getAnalysisItemID()) == null) {
                            AnalysisItem clonedItem = item.clone();
                            cleanup(clonedItem, changingDataSource);
                            replacementMap.put(item.getAnalysisItemID(), clonedItem);
                        }
                    }
                }
                for (AnalysisItem analysisItem : replacementMap.values()) {
                    System.out.println("Looking for a match for " + analysisItem.toDisplay());
                    AnalysisItem parent = dataSource.findAnalysisItemByDisplayName(analysisItem.toDisplay());
                    if (parent != null) {
                        System.out.println("\tFound match by name of " + parent.toDisplay());
                        analysisItem.setKey(parent.getKey());
                    } else {
                        System.out.println("\tNo match, looking by key");
                        Key key = dataSource.getField(analysisItem.getKey().toDisplayName());
                        if (key != null) {
                            analysisItem.setKey(key);
                        } else {
                            Key clonedKey = analysisItem.getKey().clone();
                            analysisItem.setKey(clonedKey);
                        }
                    }
                }

                ReplacementMap replacements = ReplacementMap.fromMap(replacementMap);

                for (AnalysisItem analysisItem : replacementMap.values()) {
                    analysisItem.updateIDs(replacements);
                }
                for (FilterDefinition filter : filterDefinitions) {
                    filter.updateIDs(replacements);
                }
                setFilters(filterDefinitions);
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
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

    public JSONObject toJSON(FilterHTMLMetadata metadata, List<FilterDefinition> parentFilters) throws JSONException {
        JSONObject stack = super.toJSON(metadata, parentFilters);
        List<FilterDefinition> curFilters = new ArrayList<FilterDefinition>(parentFilters);
        curFilters.addAll(getFilters());
        stack.put("type", "stack");
        stack.put("selected", defaultIndex);

        JSONArray stackItems = new JSONArray();
        stack.put("stack_items", stackItems);
        for (DashboardStackItem item : getGridItems()) {
            String label;
            if (item.getDashboardElement() instanceof DashboardReport) {
                label = ((DashboardReport) item.getDashboardElement()).getReport().getName();
            } else {
                label = item.getDashboardElement().getLabel();
            }
            JSONObject stackItem = new JSONObject();
            stackItem.put("label", label);

            stackItem.put("item", item.getDashboardElement().toJSON(metadata, curFilters));
            stackItems.put(stackItem);
        }

        return stack;
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

    public DashboardElement findElementByLabel(String label) {
        DashboardElement element = super.findElementByLabel(label);
        if (element != null) {
            return element;
        }
        for (DashboardStackItem stackItem : getGridItems()) {
            element = stackItem.getDashboardElement().findElementByLabel(label);
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
