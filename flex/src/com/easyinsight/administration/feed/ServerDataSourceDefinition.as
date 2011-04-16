package com.easyinsight.administration.feed {
import com.easyinsight.datasources.IServerDataSourceDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.ServerDataSourceDefinition")]
public class ServerDataSourceDefinition extends FeedDefinitionData implements IServerDataSourceDefinition {
    
    public function ServerDataSourceDefinition() {
        super();
    }

    public function configClass():Class {
        return null;
    }
}
}