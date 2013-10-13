/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/7/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.solutions.InsightDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardStackPositions")]
public class DashboardStackPositions {

    public var positions:Object = new Object();
    public var filterMap:Object = new Object();
    public var reports:Object = new Object();
    public var urlKey:String;

    public function DashboardStackPositions() {
    }

    public function saveReport(urlKey:String, report:InsightDescriptor):void {
        reports[urlKey] = report;
    }

    public function getReport(urlKey:String):InsightDescriptor {
        return reports[urlKey];
    }

    public function saveStackPosition(urlKey:String, selectedIndex:int):void {
        positions[urlKey] = selectedIndex;
    }

    public function getStackPosition(urlKey:String):int {
        return positions[urlKey];
    }
}
}
