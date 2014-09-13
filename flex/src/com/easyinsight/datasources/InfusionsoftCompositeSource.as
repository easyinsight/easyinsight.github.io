package com.easyinsight.datasources {
import com.easyinsight.customupload.InfusionsoftDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.infusionsoft.InfusionsoftCompositeSource")]
public class InfusionsoftCompositeSource extends CompositeServerDataSource {

    public var url:String;
    public var infusionApiKey:String;

    public function InfusionsoftCompositeSource() {
        super();
        this.feedName = "Infusionsoft";
    }

    override public function getFeedType():int {
        return DataSourceType.INFUSIONSOFT;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:InfusionsoftReportManagement = new InfusionsoftReportManagement();
        config.label = "Infusionsoft Reports";
        pages.addItem(config);
        return pages;
    }

    override public function configClass():Class {
        return InfusionsoftDataSourceCreation;
    }
}
}