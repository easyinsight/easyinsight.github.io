/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/22/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ChartReportFieldExtension")]
public class ChartReportFieldExtension extends ReportFieldExtension {

    public var goal:AnalysisItem;

    public function ChartReportFieldExtension() {
    }
}
}
