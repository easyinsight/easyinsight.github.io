/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.filtering.FilterDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardFilterOverride")]
public class DashboardFilterOverride {

    public var filterID:int;
    public var filterDefinition:FilterDefinition;
    public var hideFilter:Boolean;

    public function DashboardFilterOverride() {
    }
}
}
