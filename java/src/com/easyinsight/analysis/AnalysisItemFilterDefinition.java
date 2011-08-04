package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import org.hibernate.Session;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        super.updateIDs(replacementMap);
        if (targetItem != null) {
            targetItem = replacementMap.get(targetItem.getAnalysisItemID());
        }
        List<AnalysisItem> replaceAvailableItems = new ArrayList<AnalysisItem>();
        for (AnalysisItem availableItem : availableItems) {
            replaceAvailableItems.add(replacementMap.get(availableItem.getAnalysisItemID()));
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
        for (AnalysisItem analysisItem : availableItems) {
            analysisItem.reportSave(session);
            if (analysisItem.getKey().getKeyID() == 0) {
                session.save(getField().getKey());
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
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, int criteria) {
        List<AnalysisItem> analysisItems = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria);
        if (getEverything) {
            analysisItems.addAll(availableItems);
            analysisItems.add(targetItem);
        }
        return analysisItems;
    }
}
