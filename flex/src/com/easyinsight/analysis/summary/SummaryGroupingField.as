/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.summary {
import com.easyinsight.analysis.AnalysisItem;

import mx.collections.GroupingField;

public class SummaryGroupingField extends GroupingField {

    public var analysisItem:AnalysisItem;

    public function SummaryGroupingField(analysisItem:AnalysisItem) {
        super();
        this.analysisItem = analysisItem;
        groupingFunction = analysisLabel;
    }

    private function analysisLabel(item:Object, field:GroupingField):String {
        return analysisItem.getFormatter().format(item[analysisItem.qualifiedName()]);
    }
}
}
