package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Sep 12, 2010
 * Time: 5:10:49 PM
 */
public class MaterializedFirstValueFilter extends MaterializedFilterDefinition {

    private Value firstValue;

    public MaterializedFirstValueFilter(AnalysisItem key) {
        super(key);
    }

    @Override
    public boolean allows(Value value) {
        return true;
    }
}
