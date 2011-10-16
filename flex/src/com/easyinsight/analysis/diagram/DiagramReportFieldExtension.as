/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/11
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.diagram {
import com.easyinsight.analysis.TrendReportFieldExtension;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DiagramReportFieldExtension")]
public class DiagramReportFieldExtension extends TrendReportFieldExtension {

    public var x:int;
    public var y:int;

    public function DiagramReportFieldExtension() {
    }
}
}
