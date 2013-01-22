/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/26/11
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.TrendReportFieldExtension")]
public class TrendReportFieldExtension extends ReportFieldExtension {

    public var date:AnalysisDateDimension;
    public var iconImage:String;
    public var highLow:int;
    public var trendComparisonField:AnalysisItem;

    public function TrendReportFieldExtension() {
    }

    override public function updateFromSaved(reportFieldExtension:ReportFieldExtension):void {
        super.updateFromSaved(reportFieldExtension);
        if (date != null) {
            date.updateFromSaved(TrendReportFieldExtension(reportFieldExtension).date);
        }
        if (trendComparisonField != null) {
            trendComparisonField.updateFromSaved(TrendReportFieldExtension(reportFieldExtension).trendComparisonField);
        }
    }
}
}
