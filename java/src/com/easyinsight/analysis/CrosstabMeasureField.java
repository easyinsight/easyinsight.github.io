package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 * User: James Boe
 * Date: Apr 15, 2008
 * Time: 9:25:23 PM
 */
@Entity
@DiscriminatorValue("Measure")
public class CrosstabMeasureField extends CrosstabField {
    public CrosstabMeasureField(AnalysisItem analysisItem) {
        super(analysisItem);
    }

    public CrosstabMeasureField() {
    }
}
