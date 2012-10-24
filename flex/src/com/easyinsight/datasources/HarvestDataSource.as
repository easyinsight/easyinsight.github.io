package com.easyinsight.datasources {
import com.easyinsight.customupload.HarvestConfiguration;
import com.easyinsight.customupload.HarvestDataSourceCreation;

import mx.collections.ArrayCollection;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.harvest.HarvestCompositeSource")]
public class HarvestDataSource extends CompositeServerDataSource {

    public var url:String;
    public var username:String;
    public var password:String;
    public var accessToken:String;
    public var refreshToken:String;

    public function HarvestDataSource() {
        super();
        this.feedName = "Harvest";
    }

    override public function getFeedType():int {
        return DataSourceType.HARVEST_COMPOSITE;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:HarvestConfiguration = new HarvestConfiguration();
        config.dataSourceDefinition = this;
        config.label = "Harvest Server Configuration";
        pages.addItem(config);
        return pages;
    }

    override public function configClass():Class {
        return HarvestDataSourceCreation;
    }
}
}