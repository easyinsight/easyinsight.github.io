package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 8/1/11
 * Time: 10:08 AM
 */
public class MaterializedAnalysisItemFilterDefinition extends MaterializedFilterDefinition {
    public MaterializedAnalysisItemFilterDefinition(AnalysisItem key) {
        super(key);
    }

    @Override
    public boolean allows(Value value) {
        return true;
    }
}
