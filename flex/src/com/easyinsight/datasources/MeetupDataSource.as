package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.MeetupDataSourceCreation;
import com.easyinsight.listing.DataFeedDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.meetup.MeetupDataSource")]
public class MeetupDataSource extends ServerDataSourceDefinition {

    public var meetupAPIKey:String;

    public function MeetupDataSource() {
        super();
        feedName = "Meetup";
    }

    override public function isLiveData():Boolean {
        return false;
    }

    override public function getFeedType():int {
        return DataFeedDescriptor.MEETUP;
    }

    override public function configClass():Class {
        return MeetupDataSourceCreation;
    }
}
}