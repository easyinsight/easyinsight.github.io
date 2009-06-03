package com.easyinsight.notifications {
[Bindable]
[RemoteClass(alias="com.easyinsight.notifications.ConfigureDataFeedTodo")]
public class ConfigureDataFeedTodo extends TodoEventInfo {

    public var feedName:String;
    public var feedID:int;

    public function ConfigureDataFeedTodo() {
    }

    override public function getTitle():String {
        return feedName;
    }
}
}