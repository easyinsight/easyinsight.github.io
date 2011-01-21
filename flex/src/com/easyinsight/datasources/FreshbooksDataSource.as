package com.easyinsight.datasources {
import com.easyinsight.customupload.FreshbooksDataSourceCreation;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.freshbooks.FreshbooksCompositeSource")]
public class FreshbooksDataSource extends CompositeServerDataSource {

    public var url:String;
    public var pin:String;
    public var tokenKey:String;
    public var tokenSecretKey:String;

    public function FreshbooksDataSource() {
        super();
        feedName = "Freshbooks";
    }

    override public function isLiveData():Boolean {
        return true;
    }

    override public function getFeedType():int {
        return DataSourceType.FRESHBOOKS;
    }

    override public function configClass():Class {
        return FreshbooksDataSourceCreation;
    }
}
}