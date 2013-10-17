package com.easyinsight.datasources {
import mx.collections.ArrayCollection;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.zendesk.ZendeskCompositeSource")]
public class ZendeskDataSource extends CompositeServerDataSource {

    public var url:String;
    public var zdUserName:String;
    public var zdPassword:String;
    public var zdApiKey:String;
    public var loadComments:Boolean;

    public function ZendeskDataSource() {
        super();
        this.feedName = "Zendesk";
    }

    override public function createAdminPages():ArrayCollection {
            var pages:ArrayCollection = new ArrayCollection();

            var config:ZendeskConfiguration = new ZendeskConfiguration();
            config.dataSourceDefinition = this;
            config.label = "Zendesk Server Configuration";
            pages.addItem(config);
            return pages;
        }


    override public function getFeedType():int {
        return DataSourceType.ZENDESK;
    }

    override public function configClass():Class {
        return ZendeskDataSourceCreation;
    }


}
}