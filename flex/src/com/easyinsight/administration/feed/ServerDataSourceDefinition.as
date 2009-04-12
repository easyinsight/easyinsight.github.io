package com.easyinsight.administration.feed {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.ServerDataSourceDefinition")]
public class ServerDataSourceDefinition extends FeedDefinitionData{
    public function ServerDataSourceDefinition() {
        super();
    }

    override public function canRefresh():Boolean {
        return true;
    }
}
}