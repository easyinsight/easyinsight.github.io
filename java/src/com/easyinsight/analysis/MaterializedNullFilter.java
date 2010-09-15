package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Sep 12, 2010
 * Time: 6:41:07 PM
 */
public class MaterializedNullFilter extends MaterializedFilterDefinition {
    public MaterializedNullFilter(AnalysisItem key) {
        super(key);
    }

    @Override
    public boolean allows(Value value) {
        return value.type() == Value.EMPTY;
    }
}
