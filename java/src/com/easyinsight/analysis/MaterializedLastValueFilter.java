package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 12:43:30 PM
 */
public class MaterializedLastValueFilter extends MaterializedFilterDefinition {
    public MaterializedLastValueFilter(AnalysisItem key) {
        super(key);
    }

    public boolean allows(Value value) {
        return true;
    }
}
