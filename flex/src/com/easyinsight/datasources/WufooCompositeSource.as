package com.easyinsight.datasources {
import com.easyinsight.customupload.WufooDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.wufoo.WufooCompositeSource")]
public class WufooCompositeSource extends CompositeServerDataSource {

    public var url:String;
    public var wfApiKey:String;

    public function WufooCompositeSource() {
        super();
        this.feedName = "Wufoo";
    }

    override public function getFeedType():int {
        return DataSourceType.WUFOO;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return WufooDataSourceCreation;
    }
}
}