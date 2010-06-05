package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.DataSourceRefreshActivity")]
public class DataSourceRefreshActivity extends ScheduledActivity {

    public var dataSourceID:int;
    public var dataSourceName:String;

    public function DataSourceRefreshActivity() {
        super();
    }

    override public function get activityDisplay():String {
        return "Refresh " + dataSourceName;
    }
}
}