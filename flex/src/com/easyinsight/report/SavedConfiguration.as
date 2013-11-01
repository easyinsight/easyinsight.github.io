/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/28/13
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.report {
import com.easyinsight.dashboard.DashboardStackPositions;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.SavedConfiguration")]
public class SavedConfiguration {

    public var dashboardStackPositions:DashboardStackPositions;
    public var id:int;
    public var name:String;
    public var urlKey:String;

    public function SavedConfiguration() {
    }
}
}
