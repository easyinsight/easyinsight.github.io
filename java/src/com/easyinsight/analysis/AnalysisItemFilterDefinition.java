package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import nu.xom.Element;
import nu.xom.Nodes;
import org.hibernate.Session;

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
public class AnalysisItemFilterDefinition extends FilterDefinition {

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name="target_item_id")
    private AnalysisItem targetItem;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "analysis_item_filter_to_analysis_item",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_id", nullable = false))
    private List<AnalysisItem> availableItems;

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
                session.save(getField());
            } else {
                session.update(getField());
            }
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
    }

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, int criteria, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> analysisItems = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria, analysisItemSet, structure);
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
    public String toHTML(WSAnalysisDefinition report) {
        StringBuilder sb = new StringBuilder();
        String filterName = "filter"+getFilterID();
        String onChange = "updateFilter('filter" + getFilterID() + "')";
        sb.append(label());
        sb.append("<select id=\""+filterName+"\" onchange=\""+onChange+"\">");
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
}
