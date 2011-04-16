package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.TwitterDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.twitter.TwitterDataSource")]
public class TwitterDataSource extends ServerDataSourceDefinition {

    public var searches:ArrayCollection;

    public function TwitterDataSource() {
        super();
        this.feedName = "Twitter";
    }

    override public function getFeedType():int {
        return DataSourceType.TWITTER;
    }

    override public function configClass():Class {
        return TwitterDataSourceCreation;
    }
}
}