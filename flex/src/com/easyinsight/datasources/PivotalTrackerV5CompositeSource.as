package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.PivotalTrackerV5DataSourceCreation;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.pivotaltrackerv5.PivotalTrackerV5CompositeSource")]
public class PivotalTrackerV5CompositeSource extends CompositeServerDataSource {

    public var token:String;

    public function PivotalTrackerV5CompositeSource() {
        super();
        feedName = "Pivotal Tracker";
    }

    override public function getFeedType():int {
        return DataSourceType.PIVOTAL_TRACKER_V5;
    }

    override public function configClass():Class {
        return PivotalTrackerV5DataSourceCreation;
    }
}
}