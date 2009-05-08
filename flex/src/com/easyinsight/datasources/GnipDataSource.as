package com.easyinsight.datasources {
import com.easyinsight.administration.feed.FeedDefinitionData;

import com.easyinsight.administration.feed.ServerDataSourceDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.gnip.GnipDataSource")]
public class GnipDataSource extends ServerDataSourceDefinition{

    public var filters:ArrayCollection;

    public function GnipDataSource() {
        super();
        this.feedName = "Gnip";
    }
}
}