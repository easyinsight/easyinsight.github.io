package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 * User: James Boe
 * Date: Apr 15, 2008
 * Time: 9:25:16 PM
 */
@Entity
@DiscriminatorValue("Row")
public class CrosstabRowField extends CrosstabField {
    public CrosstabRowField(AnalysisItem analysisItem) {
        super(analysisItem);
    }

    public CrosstabRowField() {
    }
}
