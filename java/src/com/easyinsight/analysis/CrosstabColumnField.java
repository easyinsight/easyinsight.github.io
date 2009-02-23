package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 * User: James Boe
 * Date: Apr 15, 2008
 * Time: 9:25:10 PM
 */
@Entity
@DiscriminatorValue("Column")
public class CrosstabColumnField extends CrosstabField {
    public CrosstabColumnField(AnalysisItem analysisItem) {
        super(analysisItem);
    }

    public CrosstabColumnField() {
    }
}
