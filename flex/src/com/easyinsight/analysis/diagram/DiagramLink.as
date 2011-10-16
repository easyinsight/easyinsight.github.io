/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/11
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.diagram {
import com.easyinsight.analysis.AnalysisItem;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DiagramLink")]
public class DiagramLink {

    public var startItem:AnalysisItem;
    public var endItem:AnalysisItem;
    public var label:String = "";

    public function DiagramLink() {
    }
}
}
