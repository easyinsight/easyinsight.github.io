package com.easyinsight.datasources {
import com.easyinsight.customupload.FreshbooksDataSourceCreation;
import com.easyinsight.listing.DataFeedDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.freshbooks.FreshbooksCompositeSource")]
public class FreshbooksDataSource extends CompositeServerDataSource {

    public var url:String;
    public var pin:String;

    public function FreshbooksDataSource() {
        super();
        feedName = "Freshbooks";
    }

    override public function isLiveData():Boolean {
        return true;
    }

    override public function getFeedType():int {
        return DataFeedDescriptor.FRESHBOOKS;
    }

    override public function configClass():Class {
        return FreshbooksDataSourceCreation;
    }
}
}