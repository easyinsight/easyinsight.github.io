package com.easyinsight.datasources {
import com.easyinsight.administration.feed.FeedDefinitionData;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.gnip.GnipDataSource")]
public class GnipDataSource extends FeedDefinitionData{

    public var filters:ArrayCollection;

    public function GnipDataSource() {
        super();
        this.feedName = "Gnip";
    }
}
}