/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/11/13
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItem;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisItemSelection")]
public class AnalysisItemSelection {

    public var analysisItem:AnalysisItem;
    public var selected:Boolean;

    public function AnalysisItemSelection() {
    }

    public function get display():String {
        return analysisItem.display;
    }
}
}
