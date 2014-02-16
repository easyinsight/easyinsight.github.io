package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.tag.Tag;
import nu.xom.Element;
import nu.xom.Nodes;
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
 * Date: 8/1/11
 * Time: 9:43 AM
 */
@Entity
@Table(name="analysis_item_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class AnalysisItemFilterDefinition extends FilterDefinition implements IFieldChoiceFilter {

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name="target_item_id")
    private AnalysisItem targetItem;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "analysis_item_filter_to_analysis_item",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_id", nullable = false))
    private List<AnalysisItem> availableItems;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "multi_analysis_item_filter_to_analysis_item_handle",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_handle_id", nullable = false))
    private List<AnalysisItemHandle> availableHandles;

    @Transient
    private List<AnalysisItem> cachedFields;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "filter_to_field_tag",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "hibernate_tag_id", nullable = false))
    private List<WeNeedToReplaceHibernateTag> availableTags;

    public List<AnalysisItemHandle> getAvailableHandles() {
        if (availableHandles != null && availableHandles.size() > 0) {
            return availableHandles;
        }
        List<AnalysisItemHandle> handles = new ArrayList<AnalysisItemHandle>();
        if (availableItems != null) {
            for (AnalysisItem analysisItem : availableItems) {
                AnalysisItemHandle handle = new AnalysisItemHandle();
                handle.setAnalysisItemID(analysisItem.getAnalysisItemID());
                handle.setName(analysisItem.toDisplay());
                handles.add(handle);
            }
        }
        return handles;
    }

    public boolean excludeReportFields() {
        return true;
    }

    public List<AnalysisItemHandle> selectedItems() {
        return availableHandles;
    }

    public List<AnalysisItem> getCachedFields() {
        return cachedFields;
    }

    public void setCachedFields(List<AnalysisItem> cachedFields) {
        this.cachedFields = cachedFields;
    }

    public void setAvailableHandles(List<AnalysisItemHandle> availableHandles) {
        this.availableHandles = availableHandles;
    }

    @Override
    public int type() {
        return FilterDefinition.ANALYSIS_ITEM;
    }

    public List<AnalysisItemHandle> getFieldOrdering() {
        return null;
    }

    public List<WeNeedToReplaceHibernateTag> getAvailableTags() {
        return availableTags;
    }

    public void setAvailableTags(List<WeNeedToReplaceHibernateTag> availableTags) {
        this.availableTags = availableTags;
    }

    @Override
    public FilterDefinition clone() throws CloneNotSupportedException {
        AnalysisItemFilterDefinition clone = (AnalysisItemFilterDefinition) super.clone();
        clone.setAvailableHandles(new ArrayList<AnalysisItemHandle>(getAvailableHandles()));
        clone.setAvailableItems(new ArrayList<AnalysisItem>(getAvailableItems()));
        clone.setAvailableTags(new ArrayList<WeNeedToReplaceHibernateTag>(getAvailableTags()));
        return clone;
    }

    @Override
    public void updateIDs(ReplacementMap replacementMap) {
        super.updateIDs(replacementMap);
        if (targetItem != null) {
            targetItem = replacementMap.getField(targetItem);
        }
        List<AnalysisItem> replaceAvailableItems = new ArrayList<AnalysisItem>();
        for (AnalysisItem availableItem : availableItems) {
            replaceAvailableItems.add(replacementMap.getField(availableItem));
        }
        this.availableItems = replaceAvailableItems;

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
    }

    public List<AnalysisItem> getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(List<AnalysisItem> availableItems) {
        this.availableItems = availableItems;
    }

    public AnalysisItem getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(AnalysisItem targetItem) {
        this.targetItem = targetItem;
    }

    public void calculationItems(Map<String, List<AnalysisItem>> map) {
        if (getFilterName() != null && !"".equals(getFilterName())) {
            List<AnalysisItem> items = map.get(getFilterName());
            if (items == null) {
                items = new ArrayList<AnalysisItem>();
                map.put(getFilterName(), items);
            }
            items.add(getTargetItem());
        }
    }

    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedAnalysisItemFilterDefinition(getField());
    }

    @Override
    public String toQuerySQL(String tableName) {
        return null;
    }

    @Override
    public boolean validForQuery() {
        return false;
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        return start;
    }

    @Override
    public void beforeSave(Session session) {
        super.beforeSave(session);
        targetItem.reportSave(session);
        if (targetItem.getKey().getKeyID() == 0) {
            session.save(getField().getKey());
        }
        if (getField().getAnalysisItemID() == 0) {
            session.save(getField());
        } else {
            session.update(getField());
        }
        for (AnalysisItem analysisItem : availableItems) {
            analysisItem.reportSave(session);
            if (analysisItem.getKey().getKeyID() == 0) {
                session.save(getField().getKey());
            }
            if (analysisItem.getAnalysisItemID() == 0) {
                session.save(analysisItem);
            } else {
                session.update(analysisItem);
            }
        }
        for (AnalysisItemHandle analysisItem : availableHandles) {
            analysisItem.save();
        }
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        setTargetItem((AnalysisItem) Database.deproxy(getTargetItem()));
        getTargetItem().afterLoad();
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        for (AnalysisItem item : getAvailableItems()) {
            AnalysisItem validItem = (AnalysisItem) Database.deproxy(item);
            validItem.afterLoad();
            items.add(validItem);
        }
        setAvailableItems(items);
        setAvailableHandles(new ArrayList<AnalysisItemHandle>(getAvailableHandles()));
        setAvailableTags(new ArrayList<WeNeedToReplaceHibernateTag>(getAvailableTags()));
    }

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> analysisItems = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);
        if (getEverything) {
            analysisItems.addAll(availableItems);
            analysisItems.add(targetItem);
        }
        return analysisItems;
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        Element targetItemXML = targetItem.toXML(xmlMetadata);
        element.appendChild(targetItemXML);
        Element availableItemsElement = new Element("availableItems");
        element.appendChild(availableItemsElement);
        for (AnalysisItem analysisItem : availableItems) {
            availableItemsElement.appendChild(analysisItem.toXML(xmlMetadata));
        }
        return element;
    }

    public void customFromXML(Element element, XMLImportMetadata xmlImportMetadata) {
        Nodes nodes = element.query("availableItems/");
        for (int i = 0; i < nodes.size(); i++) {

        }
    }

    @Override
    public String toHTML(FilterHTMLMetadata filterHTMLMetadata) {
        StringBuilder sb = new StringBuilder();
        String filterName = "filter"+getFilterID();
        String key = filterHTMLMetadata.getFilterKey();
        String function = filterHTMLMetadata.createOnChange();
        String onChange = "updateFilter('" + filterName + "','" + key + "', " + function + ")";
        sb.append(label(true));
        sb.append("<select class=\"filterSelect\" id=\""+filterName+"\" onchange=\""+onChange+"\">");
        for (AnalysisItem analysisItem : getAvailableItems()) {
            if (availableItems.equals(targetItem)) {
                sb.append("<option selected=\"selected\" value=\""+analysisItem.getAnalysisItemID()+"\">").append(analysisItem.toDisplay()).append("</option>");
            } else {
                sb.append("<option value=\""+analysisItem.getAnalysisItemID()+"\">").append(analysisItem.toDisplay()).append("</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    @Override
    public JSONObject toJSON(FilterHTMLMetadata filterHTMLMetadata) throws JSONException {
        JSONObject jo = super.toJSON(filterHTMLMetadata);
        List<AnalysisItemSelection> itemsAvailable = new DataService().possibleFields(this, null, null, null);
        jo.put("type", "field_filter");
        jo.put("selected", String.valueOf(targetItem.getAnalysisItemID()));
        JSONArray available = new JSONArray();
        for(AnalysisItemSelection analysisItem : itemsAvailable) {
            JSONObject j = new JSONObject();
            j.put("value", analysisItem.getAnalysisItem().getAnalysisItemID());
            j.put("label", analysisItem.getAnalysisItem().toUnqualifiedDisplay());
            available.put(j);
        }
        jo.put("values", available);
        return jo;
    }

    @Override
    public void override(FilterDefinition overrideFilter) {
        AnalysisItemFilterDefinition f = (AnalysisItemFilterDefinition) overrideFilter;
        setTargetItem(f.getTargetItem());
        setAvailableItems(f.getAvailableItems());
    }
}
