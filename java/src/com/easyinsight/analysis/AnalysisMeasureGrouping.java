package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.List;
import java.util.Collection;

/**
 * User: jamesboe
 * Date: Sep 25, 2009
 * Time: 12:06:03 AM
 */
@Entity
@Table(name="analysis_measure_grouping")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisMeasureGrouping extends AnalysisDimension {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "analysis_measure_grouping_join",
            joinColumns = @JoinColumn(name = "analysis_item_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "measure_id", nullable = false))
    private List<AnalysisMeasure> measures;

    public List<AnalysisMeasure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisMeasure> measures) {
        this.measures = measures;
    }

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, boolean completelyShallow) {
        List<AnalysisItem> items = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, completelyShallow);
        items.addAll(measures);
        return items;
    }

    public void blah() {
        for (AnalysisMeasure measure : measures) {

        }
    }
}
