/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/7/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import mx.collections.ArrayCollection;

public class DashboardStackPositions {

    public var stackPositions:Object = new Object();

    public function DashboardStackPositions() {
    }

    public function saveStackPosition(dashboardStack:DashboardStack, selectedIndex:int):void {
        stackPositions[String(dashboardStack.elementServerID)] = selectedIndex;
    }

    public function getStackPosition(dashboardStack:DashboardStack):int {
        return stackPositions[String(dashboardStack.elementServerID)];
    }
}
}
