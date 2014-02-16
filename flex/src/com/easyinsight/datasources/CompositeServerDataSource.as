package com.easyinsight.datasources {
import com.easyinsight.feedassembly.CompositeFeedDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.composite.CompositeServerDataSource")]
public class CompositeServerDataSource extends CompositeFeedDefinition implements IServerDataSourceDefinition {

    public var addonConnections:ArrayCollection;

    public function CompositeServerDataSource() {
        super();
    }

    public function configClass():Class {
        return null;
    }
}
}