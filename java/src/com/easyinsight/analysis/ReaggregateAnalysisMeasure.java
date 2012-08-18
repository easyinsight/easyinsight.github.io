package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.pipeline.CleanupComponent;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 11:05:07 AM
 */

@Entity
@Table(name="reaggregate_analysis_measure")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class ReaggregateAnalysisMeasure extends AnalysisMeasure {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wrapped_measure_id")
    private AnalysisItem wrappedMeasure;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="aggregate_item_id")
    private AnalysisItem aggregationItem;

    public int getType() {
        return super.getType() | AnalysisItemTypes.REAGGREGATE_MEASURE;
    }

    public AnalysisItem getWrappedMeasure() {
        return wrappedMeasure;
    }

    public void setWrappedMeasure(AnalysisItem wrappedMeasure) {
        this.wrappedMeasure = wrappedMeasure;
    }

    public AnalysisItem getAggregationItem() {
        return aggregationItem;
    }

    public void setAggregationItem(AnalysisItem aggregationItem) {
        this.aggregationItem = aggregationItem;
    }

    @Override
    public boolean isDerived() {
        return true;
    }

    public void reportSave(Session session) {
        super.reportSave(session);
        if (aggregationItem.getAnalysisItemID() == 0) {
            aggregationItem.reportSave(session);
            session.save(aggregationItem);
        }
        if (wrappedMeasure.getAnalysisItemID() == 0) {
            wrappedMeasure.reportSave(session);
            session.save(wrappedMeasure);
        }
    }

    @Override
    public void beforeSave() {
        super.beforeSave();
        aggregationItem.beforeSave();
        wrappedMeasure.beforeSave();
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        setAggregationItem((AnalysisDimension) Database.deproxy(getAggregationItem()));
        getAggregationItem().afterLoad();
        setWrappedMeasure((AnalysisItem) Database.deproxy(getWrappedMeasure()));
        getWrappedMeasure().afterLoad();
    }
}
