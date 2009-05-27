package com.easyinsight.datasources {
import com.easyinsight.administration.feed.FeedDefinitionData;

import com.easyinsight.administration.feed.ServerDataSourceDefinition;

import com.easyinsight.customupload.GnipDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.gnip.GnipDataSource")]
public class GnipDataSource extends ServerDataSourceDefinition{

    public var filters:ArrayCollection;

    public function GnipDataSource() {
        super();
        this.feedName = "Gnip";
    }

    override public function configClass():Class {
        return GnipDataSourceCreation;
    }

    override public function canRefresh():Boolean {
        return true;
    }
}
}