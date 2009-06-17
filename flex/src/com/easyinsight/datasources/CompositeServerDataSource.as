package com.easyinsight.datasources {
import com.easyinsight.feedassembly.CompositeFeedDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.composite.CompositeServerDataSource")]
public class CompositeServerDataSource extends CompositeFeedDefinition {

    public var credentialsDefinition:int;

    public function CompositeServerDataSource() {
        super();
    }

    public function configClass():Class {
        return null;
    }
}
}