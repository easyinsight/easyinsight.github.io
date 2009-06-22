package com.easyinsight.administration.feed {
import com.easyinsight.datasources.IServerDataSourceDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.ServerDataSourceDefinition")]
public class ServerDataSourceDefinition extends FeedDefinitionData implements IServerDataSourceDefinition {

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