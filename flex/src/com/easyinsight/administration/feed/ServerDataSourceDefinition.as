package com.easyinsight.administration.feed {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.ServerDataSourceDefinition")]
public class ServerDataSourceDefinition extends FeedDefinitionData{

    public var credentialsDefinition:int;
    
    public function ServerDataSourceDefinition() {
        super();
    }

    override public function canRefresh():Boolean {
        return true;
    }

    public function configClass():Class {
        return null;
    }
}
}