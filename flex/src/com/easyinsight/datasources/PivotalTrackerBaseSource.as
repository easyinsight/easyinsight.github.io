package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.PivotalTrackerDataSourceCreation;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.pivotaltracker.PivotalTrackerBaseSource")]
public class PivotalTrackerBaseSource extends ServerDataSourceDefinition {

    public var ptUserName:String;
    public var ptPassword:String;

    public function PivotalTrackerBaseSource() {
        super();
        feedName = "Pivotal Tracker";
    }

    override public function getFeedType():int {
        return DataSourceType.PIVOTAL_TRACKER;
    }

    override public function configClass():Class {
        return PivotalTrackerDataSourceCreation;
    }
}
}