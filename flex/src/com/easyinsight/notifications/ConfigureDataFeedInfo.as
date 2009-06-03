package com.easyinsight.notifications {
[Bindable]
[RemoteClass(alias="com.easyinsight.notifications.ConfigureDataFeedInfo")]
public class ConfigureDataFeedInfo extends TodoEventInfo {

    public var feedName:String;
    public var feedID:int;

    public function ConfigureDataFeedInfo() {
    }

    override public function getTitle():String {
        return "Configure " + feedName + " to match with your service's settings.";
    }
}
}