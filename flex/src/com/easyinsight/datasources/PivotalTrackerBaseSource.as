package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.PivotalTrackerDataSourceCreation;
import com.easyinsight.listing.DataFeedDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.pivotaltracker.PivotalTrackerBaseSource")]
public class PivotalTrackerBaseSource extends ServerDataSourceDefinition {
    public function PivotalTrackerBaseSource() {
        super();
        feedName = "Pivotal Tracker";
    }

    override public function isLiveData():Boolean {
        return false;
    }

    override public function getFeedType():int {
        return DataFeedDescriptor.PIVOTAL_TRACKER;
    }

    override public function configClass():Class {
        return PivotalTrackerDataSourceCreation;
    }
}
}