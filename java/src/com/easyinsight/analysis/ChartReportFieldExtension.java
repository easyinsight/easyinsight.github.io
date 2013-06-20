package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * User: jamesboe
 * Date: 5/22/13
 * Time: 2:03 PM
 */
@Entity
@Table(name="chart_report_field_extension")
@PrimaryKeyJoinColumn(name="report_field_extension_id")
public class ChartReportFieldExtension extends ReportFieldExtension {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="goal_field")
    private AnalysisItem goal;

    public AnalysisItem getGoal() {
        return goal;
    }

    public void setGoal(AnalysisItem goal) {
        this.goal = goal;
    }

    @Override
    public void reportSave(Session session) {
        super.reportSave(session);
        if (goal != null) {
            goal.reportSave(session);
            session.saveOrUpdate(goal);
        }
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        if (goal != null) {
            setGoal((AnalysisItem) Database.deproxy(getGoal()));
            goal.afterLoad();
        }
    }

    @Override
    public void updateIDs(ReplacementMap replacementMap) {
        super.updateIDs(replacementMap);
        if (goal != null) {
            goal = replacementMap.getField(goal);
        }
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> items = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);
        if (getEverything && goal != null) {
            items.addAll(goal.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure));
        }
        return items;
    }
}
