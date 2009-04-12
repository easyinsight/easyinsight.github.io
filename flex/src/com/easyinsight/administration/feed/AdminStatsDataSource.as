package com.easyinsight.administration.feed {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.admin.AdminStatsDataSource")]
public class AdminStatsDataSource extends ServerDataSourceDefinition{
    public function AdminStatsDataSource() {
        super();
        this.feedName = "Admin Stats";
    }
}
}