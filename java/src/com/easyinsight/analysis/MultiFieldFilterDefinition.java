package com.easyinsight.analysis;

import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 11/7/13
 * Time: 7:59 AM
 */
@Entity
@Table(name="multi_analysis_item_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class MultiFieldFilterDefinition extends FilterDefinition {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "multi_analysis_item_filter_to_analysis_item_handle",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_handle_id", nullable = false))
    private List<AnalysisItemHandle> availableItems;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "selected_analysis_item_filter_to_analysis_item_handle",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_handle_id", nullable = false))
    private List<AnalysisItemHandle> selectedItems;

    @Column(name="show_all")
    private boolean all;

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public List<AnalysisItemHandle> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<AnalysisItemHandle> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<AnalysisItemHandle> getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(List<AnalysisItemHandle> availableItems) {
        this.availableItems = availableItems;
    }

    @Override
    public int type() {
        return FilterDefinition.MULTI_FIELD;
    }

    @Override
    public String toQuerySQL(String tableName) {
        return null;
    }

    @Override
    public boolean validForQuery() {
        return false;
    }

    public void calculationItems(Map<String, List<AnalysisItem>> map) {
        /*if (getFilterName() != null && !"".equals(getFilterName())) {
            List<AnalysisItem> items = map.get(getFilterName());
            if (items == null) {
                items = new ArrayList<AnalysisItem>();
                map.put(getFilterName(), items);
            }
            items.add(getTargetItem());
        }*/
    }

    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedAnalysisItemFilterDefinition(getField());
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        return start;
    }

    @Override
    public void beforeSave(Session session) {
        super.beforeSave(session);
        for (AnalysisItemHandle analysisItem : availableItems) {
            analysisItem.save(session);
            /*analysisItem.reportSave(session);
            if (analysisItem.getKey().getKeyID() == 0) {
                session.save(getField().getKey());
            }
            if (analysisItem.getAnalysisItemID() == 0) {
                session.save(analysisItem);
            } else {
                session.update(analysisItem);
            }*/
        }
        for (AnalysisItemHandle analysisItem : selectedItems) {
            analysisItem.save(session);
            /*if (analysisItem.getKey().getKeyID() == 0) {
                session.save(getField().getKey());
            }
            if (analysisItem.getAnalysisItemID() == 0) {
                session.save(analysisItem);
            } else {
                session.update(analysisItem);
            }*/
        }
    }

    @Override
    public FilterDefinition clone() throws CloneNotSupportedException {
        MultiFieldFilterDefinition multiFieldFilterDefinition = (MultiFieldFilterDefinition) super.clone();
        List<AnalysisItemHandle> replaceAvailableItems = new ArrayList<AnalysisItemHandle>();
        for (AnalysisItemHandle availableItem : availableItems) {
            replaceAvailableItems.add(availableItem.clone());
        }
        multiFieldFilterDefinition.availableItems = replaceAvailableItems;

        List<AnalysisItemHandle> replaceSelectedItems = new ArrayList<AnalysisItemHandle>();
        for (AnalysisItemHandle selectedItem : selectedItems) {
            replaceSelectedItems.add(selectedItem.clone());
        }
        multiFieldFilterDefinition.selectedItems = replaceSelectedItems;
        return multiFieldFilterDefinition;
    }

    /*@Override
    public void afterLoad() {
        super.afterLoad();
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        for (AnalysisItem item : getAvailableItems()) {
            AnalysisItem validItem = (AnalysisItem) Database.deproxy(item);
            validItem.afterLoad();
            items.add(validItem);
        }
        setAvailableItems(items);

        List<AnalysisItem> selectedItems = new ArrayList<AnalysisItem>();
        for (AnalysisItem item : getSelectedItems()) {
            AnalysisItem validItem = (AnalysisItem) Database.deproxy(item);
            validItem.afterLoad();
            selectedItems.add(validItem);
        }
        setSelectedItems(selectedItems);
    }*/

    public void afterLoad() {
        super.afterLoad();
        setAvailableItems(new ArrayList<AnalysisItemHandle>(getAvailableItems()));
        setSelectedItems(new ArrayList<AnalysisItemHandle>(getSelectedItems()));
    }

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        /*List<AnalysisItem> analysisItems = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);
        if (getEverything) {
            analysisItems.addAll(availableItems);
            analysisItems.addAll(selectedItems);
        }
        return analysisItems;*/
        return new ArrayList<AnalysisItem>();
    }

    @Override
    public JSONObject toJSON(FilterHTMLMetadata filterHTMLMetadata) throws JSONException {
        JSONObject jo = super.toJSON(filterHTMLMetadata);
        WSAnalysisDefinition report = filterHTMLMetadata.getReport();
        List<AnalysisItemSelection> itemsAvailable = new DataService().possibleFields(this, report);

        jo.put("type", "multi_field_filter");
        jo.put("count", itemsAvailable.size());

        List<String> stringList = new ArrayList<String>();
        JSONObject existingChoices = new JSONObject();
        for (AnalysisItemSelection selection : itemsAvailable) {
            stringList.add(selection.getAnalysisItem().toDisplay());
            if (selection.isSelected()) {
                existingChoices.put(selection.getAnalysisItem().toDisplay(), true);
            }
        }
        if (isAll()) {
            existingChoices.put("All", true);
        }
        Collections.sort(stringList);

        JSONArray arr = new JSONArray(stringList);
        jo.put("values", arr);

        jo.put("selected", existingChoices);
        return jo;
    }
}
