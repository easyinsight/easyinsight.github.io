package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisItem;

import mx.collections.GroupingField;

public class CustomGroupingField extends GroupingField {

    public var analysisItem:AnalysisItem;

    public function CustomGroupingField(analysisItem:AnalysisItem) {
        super();
        this.analysisItem = analysisItem;
        groupingFunction = analysisLabel;
    }

    private function analysisLabel(item:Object, field:GroupingField):String {
        return analysisItem.getFormatter().format(item[analysisItem.qualifiedName()]);
    }
}
}