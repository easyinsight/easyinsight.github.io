package com.easyinsight.datasources {
import com.easyinsight.customupload.Batchbook2DataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.batchbook2.Batchbook2CompositeSource")]
public class Batchbook2DataSource extends CompositeServerDataSource {

    public var url:String;
    public var token:String;

    public function Batchbook2DataSource() {
        super();
        this.feedName = "Batchbook";
    }

    override public function getFeedType():int {
        return DataSourceType.BATCHBOOK2;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return Batchbook2DataSourceCreation;
    }
}
}