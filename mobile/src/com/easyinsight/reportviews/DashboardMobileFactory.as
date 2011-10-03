/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/11
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.dashboard.DashboardEditorMetadata;
import com.easyinsight.dashboard.DashboardElement;
import com.easyinsight.dashboard.DashboardGrid;
import com.easyinsight.dashboard.DashboardImage;
import com.easyinsight.dashboard.DashboardReport;
import com.easyinsight.dashboard.DashboardScorecard;
import com.easyinsight.dashboard.DashboardStack;
import com.easyinsight.dashboard.IDashboardViewComponent;

public class DashboardMobileFactory {

    private static var counter:int = 0;

    public function DashboardMobileFactory() {
    }

    public static function createViewUIComponent(element:DashboardElement, dashboardEditorMetadata:DashboardEditorMetadata):IDashboardViewComponent {
        if (element is DashboardGrid) {
            var gComp:DashboardGridMobileComponent = new DashboardGridMobileComponent();
            gComp.dashboardGrid = element as DashboardGrid;
            gComp.dashboardEditorMetadata = dashboardEditorMetadata;
            return gComp;
        } else if (element is DashboardStack) {
            var sComp:DashboardStackMobileComponent = new DashboardStackMobileComponent();
            sComp.dashboardStack = element as DashboardStack;
            sComp.elementID = String(counter++);
            sComp.dashboardEditorMetadata = dashboardEditorMetadata;
            return sComp;
        } else if (element is DashboardImage) {
        } else if (element is DashboardReport) {
            var rComp:DashboardReportMobileComponent = new DashboardReportMobileComponent();
            rComp.dashboardReport = element as DashboardReport;
            return rComp;
        } else if (element is DashboardScorecard) {
        }
        return null;
    }
}
}
