package com.easyinsight.analysis;

import com.easyinsight.tag.Tag;
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
public class MultiFieldFilterDefinition extends FilterDefinition implements IFieldChoiceFilter {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "multi_analysis_item_filter_to_analysis_item_handle",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_handle_id", nullable = false))
    private List<AnalysisItemHandle> availableHandles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "selected_analysis_item_filter_to_analysis_item_handle",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_handle_id", nullable = false))
    private List<AnalysisItemHandle> selectedItems;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "filter_to_field_tag",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "hibernate_tag_id", nullable = false))
    private List<WeNeedToReplaceHibernateTag> availableTags;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "ordering_analysis_item_filter_to_analysis_item_handle",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_handle_id", nullable = false))
    private List<AnalysisItemHandle> fieldOrdering;

    @Column(name="exclude_report_fields")
    private boolean excludeReportFields;

    @Column(name="alpha_sort")
    private boolean alphaSort;

    @Column(name="show_all")
    private boolean all;

    public boolean isAlphaSort() {
        return alphaSort;
    }

    public void setAlphaSort(boolean alphaSort) {
        this.alphaSort = alphaSort;
    }

    public List<AnalysisItemHandle> getFieldOrdering() {
        return fieldOrdering;
    }

    public void setFieldOrdering(List<AnalysisItemHandle> fieldOrdering) {
        this.fieldOrdering = fieldOrdering;
    }

    public List<WeNeedToReplaceHibernateTag> getAvailableTags() {
        return availableTags;
    }

    public void setAvailableTags(List<WeNeedToReplaceHibernateTag> availableTags) {
        this.availableTags = availableTags;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean isExcludeReportFields() {
        return excludeReportFields;
    }

    public void setExcludeReportFields(boolean excludeReportFields) {
        this.excludeReportFields = excludeReportFields;
    }

    public List<AnalysisItemHandle> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<AnalysisItemHandle> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<AnalysisItemHandle> getAvailableHandles() {
        return availableHandles;
    }

    public boolean excludeReportFields() {
        return excludeReportFields;
    }

    public List<AnalysisItemHandle> selectedItems() {
        return selectedItems;
    }

    public void setAvailableHandles(List<AnalysisItemHandle> availableItems) {
        this.availableHandles = availableItems;
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
        for (AnalysisItemHandle analysisItem : availableHandles) {
            analysisItem.save();
        }
        for (AnalysisItemHandle analysisItem : selectedItems) {
            analysisItem.save();
        }
        for (AnalysisItemHandle analysisItem : fieldOrdering) {
            analysisItem.save();
        }
    }

    @Override
    public void updateIDs(ReplacementMap replacementMap) {
        super.updateIDs(replacementMap);

        List<AnalysisItemHandle> replaceAvailableHandles = new ArrayList<AnalysisItemHandle>();
        for (AnalysisItemHandle availableItem : availableHandles) {
            AnalysisItemHandle newHandle = new AnalysisItemHandle();
            newHandle.setName(availableItem.getName());
            replaceAvailableHandles.add(newHandle);
        }
        this.availableHandles = replaceAvailableHandles;

        List<WeNeedToReplaceHibernateTag> replaceTags = new ArrayList<WeNeedToReplaceHibernateTag>();
        for (WeNeedToReplaceHibernateTag tag : availableTags) {
            Tag newTag1 = replacementMap.findReplacementTag(tag.getTagID());
            WeNeedToReplaceHibernateTag newTag = new WeNeedToReplaceHibernateTag();
            newTag.setTagID(newTag1.getId());
            replaceTags.add(newTag);
        }
        this.availableTags = replaceTags;

        List<AnalysisItemHandle> replaceFieldOrdering = new ArrayList<AnalysisItemHandle>();
        for (AnalysisItemHandle availableItem : fieldOrdering) {
            AnalysisItemHandle newHandle = new AnalysisItemHandle();
            newHandle.setName(availableItem.getName());
            replaceFieldOrdering.add(newHandle);
        }
        this.fieldOrdering = replaceFieldOrdering;

        List<AnalysisItemHandle> replaceSelectedFields = new ArrayList<AnalysisItemHandle>();
        for (AnalysisItemHandle availableItem : selectedItems) {
            AnalysisItemHandle newHandle = new AnalysisItemHandle();
            newHandle.setName(availableItem.getName());
            newHandle.setSelected(availableItem.isSelected());
            replaceSelectedFields.add(newHandle);
        }
        this.selectedItems = replaceSelectedFields;
    }

    public void afterLoad() {
        super.afterLoad();
        setAvailableHandles(new ArrayList<AnalysisItemHandle>(getAvailableHandles()));
        setSelectedItems(new ArrayList<AnalysisItemHandle>(getSelectedItems()));
        setAvailableTags(new ArrayList<WeNeedToReplaceHibernateTag>(getAvailableTags()));
        setFieldOrdering(new ArrayList<AnalysisItemHandle>(getFieldOrdering()));
    }

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        return new ArrayList<AnalysisItem>();
    }

    @Override
    public JSONObject toJSON(FilterHTMLMetadata filterHTMLMetadata) throws JSONException {
        JSONObject jo = super.toJSON(filterHTMLMetadata);
        List<AnalysisItemSelection> itemsAvailable = new DataService().possibleFields(this, null, null, null);

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
        //Collections.sort(stringList);

        JSONArray arr = new JSONArray(stringList);
        jo.put("values", arr);

        jo.put("selected", existingChoices);
        return jo;
    }

    @Override
    public void override(FilterDefinition overrideFilter) {
        super.override(overrideFilter);
        MultiFieldFilterDefinition overrideFilterDefinition = (MultiFieldFilterDefinition) overrideFilter;
        setSelectedItems(overrideFilterDefinition.getSelectedItems());
    }
}
