package com.easyinsight.datasources {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.freshdesk.FreshdeskCompositeSource")]
public class FreshdeskDataSource extends CompositeServerDataSource {

    public var url:String;
    public var freshdeskApiKey:String;

    public function FreshdeskDataSource() {
        super();
        this.feedName = "Freshdesk";
    }

    override public function createAdminPages():ArrayCollection {
            var pages:ArrayCollection = new ArrayCollection();
            return pages;
        }


    override public function getFeedType():int {
        return DataSourceType.FRESHDESK;
    }

    override public function configClass():Class {
        return FreshdeskDataSourceCreation;
    }


}
}