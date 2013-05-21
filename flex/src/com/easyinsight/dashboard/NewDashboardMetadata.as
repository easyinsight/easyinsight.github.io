/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/14/13
 * Time: 8:40 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.datasources.DataSourceInfo;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.NewDashboardMetadata")]
public class NewDashboardMetadata {

    public var availableReports:ArrayCollection;
    public var dataSourceInfo:DataSourceInfo;

    public function NewDashboardMetadata() {
    }
}
}
