package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 * User: James Boe
 * Date: Apr 15, 2008
 * Time: 11:36:33 PM
 */
@Entity
@DiscriminatorValue("Dimension")
public class ChartDimensionField extends ChartField {

    public ChartDimensionField(AnalysisItem analysisItem) {
        super(analysisItem);
    }

    public ChartDimensionField() {
    }
}
