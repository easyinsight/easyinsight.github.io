package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.DataSourceRefreshActivity")]
public class DataSourceRefreshActivity extends ScheduledActivity {

    public static const DAILY:int = 0;
    public static const HOURLY:int = 1;

    public var dataSourceID:int;
    public var dataSourceName:String;
    public var intervalUnits:int;
    public var intervalNumber:int;

    public function DataSourceRefreshActivity() {
        super();
    }

    override public function get activityDisplay():String {
        return "Refresh " + dataSourceName;
    }
}
}