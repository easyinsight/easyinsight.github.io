package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 * User: James Boe
 * Date: Apr 15, 2008
 * Time: 11:36:27 PM
 */
@Entity
@DiscriminatorValue("Measure")
public class ChartMeasureField extends ChartField {
    public ChartMeasureField(AnalysisItem analysisItem) {
        super(analysisItem);
    }

    public ChartMeasureField() {
    }
}
