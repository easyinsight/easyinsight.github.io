package com.easyinsight.datasources {


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.zendesk.ZendeskCompositeSource")]
public class ZendeskDataSource extends CompositeServerDataSource {

    public var url:String;
    public var zdUserName:String;
    public var zdPassword:String;

    public function ZendeskDataSource() {
        super();
        this.feedName = "Zendesk";
    }

    override public function isLiveData():Boolean {
        return false;
    }

    override public function getFeedType():int {
        return DataSourceType.ZENDESK;
    }

    override public function configClass():Class {
        return ZendeskDataSourceCreation;
    }
}
}