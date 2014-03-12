package com.easyinsight.datasources {

import com.easyinsight.customupload.RedboothDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.redbooth.RedboothCompositeSource")]
public class RedboothCompositeSource extends CompositeServerDataSource {

    public var accessToken:String;
    public var refreshToken:String;

    public function RedboothCompositeSource() {
        super();
        this.feedName = "Redbooth";
    }

    override public function getFeedType():int {
        return DataSourceType.REDBOOTH;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return RedboothDataSourceCreation;
    }
}
}