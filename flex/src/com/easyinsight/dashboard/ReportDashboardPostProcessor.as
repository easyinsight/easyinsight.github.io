/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/23/13
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IReportPostProcessor;

import mx.collections.ArrayCollection;

public class ReportDashboardPostProcessor implements IReportPostProcessor {

    private var dashboardEditorMetadata:DashboardEditorMetadata;

    public function ReportDashboardPostProcessor(dashboardEditorMetadata:DashboardEditorMetadata) {
        this.dashboardEditorMetadata = dashboardEditorMetadata;
    }

    public function processReport(report:AnalysisDefinition):void {
        if (dashboardEditorMetadata.dashboard.defaultDrillthrough != null) {
            for each (var item:AnalysisItem in report.getFields()) {
                if (item.links == null) {
                    item.links = new ArrayCollection();
                }
                if (item.links.length == 0) {
                    item.links.addItem(dashboardEditorMetadata.dashboard.defaultDrillthrough);
                }
            }
        }
    }
}
}
