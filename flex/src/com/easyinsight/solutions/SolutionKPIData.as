package com.easyinsight.solutions {
import com.easyinsight.schedule.DataSourceRefreshActivity;

[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.SolutionKPIData")]
public class SolutionKPIData {

    public var addDataSourceToGroup:Boolean;
    public var activity:DataSourceRefreshActivity;
    public var dataSourceID:int;
    public var utcOffset:int;

    public function SolutionKPIData() {
    }
}
}