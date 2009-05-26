package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.*;

import com.easyinsight.core.Key;
import com.easyinsight.database.Database;
import org.hibernate.Session;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 10:09:05 AM
 */
@Entity
@Table(name = "analysis_step")
public class AnalysisStep extends AnalysisDateDimension {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_date_dimension_id")
    private AnalysisDateDimension startDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_date_dimension_id")
    private AnalysisDateDimension endDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correlation_dimension_id")
    private AnalysisDimension correlationDimension;

    public AnalysisStep() {
    }

    public AnalysisStep(Key key, boolean group, int dateLevel, AnalysisDateDimension startDate, AnalysisDateDimension endDate, AnalysisDimension correlationDimension) {
        super(key, group, dateLevel);
        this.startDate = startDate;
        this.endDate = endDate;
        this.correlationDimension = correlationDimension;
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.STEP;
    }

    public AnalysisDateDimension getStartDate() {
        return startDate;
    }

    @Override
    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        super.updateIDs(replacementMap);
        if (startDate != null) {
            setStartDate((AnalysisDateDimension) replacementMap.get(startDate.getAnalysisItemID()));
        }
        if (endDate != null) {
            setEndDate((AnalysisDateDimension) replacementMap.get(endDate.getAnalysisItemID()));
        }
        if (correlationDimension != null) {
            setCorrelationDimension((AnalysisDimension) replacementMap.get(correlationDimension.getAnalysisItemID()));
        }
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        if (startDate != null) {
            setStartDate((AnalysisDateDimension) Database.deproxy(getStartDate()));
        }
        if (endDate != null) {
            setEndDate((AnalysisDateDimension) Database.deproxy(getEndDate()));
        }
        if (correlationDimension != null) {
            setCorrelationDimension((AnalysisDimension) Database.deproxy(getCorrelationDimension()));
        }
    }

    public void setStartDate(AnalysisDateDimension startDate) {
        this.startDate = startDate;
    }

    public AnalysisDateDimension getEndDate() {
        return endDate;
    }

    public void setEndDate(AnalysisDateDimension endDate) {
        this.endDate = endDate;
    }

    public AnalysisDimension getCorrelationDimension() {
        return correlationDimension;
    }

    public void setCorrelationDimension(AnalysisDimension correlationDimension) {
        this.correlationDimension = correlationDimension;
    }

    @Override
    public boolean isVirtual() {
        return true;
    }

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(startDate);
        startDate.setDateLevel(getDateLevel());
        items.add(endDate);
        endDate.setDateLevel(getDateLevel());
        items.add(correlationDimension);
        return items;
    }

    @Override
    public boolean isDerived() {
        return true;
    }

    @Override
    public boolean isValid() {
        return startDate != null && endDate != null && correlationDimension != null;
    }

    @Override
    public void reportSave(Session session) {
        super.reportSave(session);
        session.saveOrUpdate(startDate);
        session.saveOrUpdate(endDate);
        session.saveOrUpdate(correlationDimension);
    }
}
