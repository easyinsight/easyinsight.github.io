/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 4/28/11
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {

import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.framework.User;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;
import mx.events.MenuEvent;

public class TopReportsDropdown extends ArghButton {

    public function TopReportsDropdown() {
        dataProvider = User.getInstance().topReports;
        labelField = "name";
        styleName = "flatTransparentHeaderBarButton";
        setStyle("popUpStyleName", "dropAreaPopup");
        label = "Reports";
        addEventListener(MenuEvent.ITEM_CLICK, onItemClick);
    }

    private function onItemClick(event:MenuEvent):void {
        var desc:EIDescriptor = event.item as EIDescriptor;
        if (desc is InsightDescriptor) {
            User.getEventNotifier().dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(InsightDescriptor(desc))));
        } else if (desc is DashboardDescriptor) {
            User.getEventNotifier().dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_VIEW, {dashboardID: DashboardDescriptor(desc).id})));
        }
    }
}
}
