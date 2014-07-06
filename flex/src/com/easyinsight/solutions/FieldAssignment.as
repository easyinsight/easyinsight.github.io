/**
 * Created by jamesboe on 7/3/14.
 */
package com.easyinsight.solutions {
import com.easyinsight.analysis.AnalysisItem;

[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.FieldAssignment")]
public class FieldAssignment {

    public var sourceField:AnalysisItem;
    public var targetField:AnalysisItem;

    public function FieldAssignment() {
    }

    public function get display():String {
        return sourceField.display;
    }
}
}
