/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/26/13
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.solutions.InsightDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardInfo")]
public class DashboardInfo {

    public var dashboardID:int;
    public var dashboardStackPositions:DashboardStackPositions;
    public var report:InsightDescriptor;

    public function DashboardInfo() {
    }
}
}
