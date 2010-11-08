package com.easyinsight.datasources {
import com.easyinsight.customupload.ConstantContactDataSourceCreation;
import com.easyinsight.listing.DataFeedDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.constantcontact.ConstantContactCompositeSource")]
public class ConstantContactDataSource extends CompositeServerDataSource {

    public var pin:String;
    public var ccUserName:String;

    public function ConstantContactDataSource() {
        super();
        feedName = "Constant Contact";
    }

    override public function isLiveData():Boolean {
        return true;
    }

    override public function getFeedType():int {
        return DataFeedDescriptor.CONSTANT_CONTACT;
    }

    override public function configClass():Class {
        return ConstantContactDataSourceCreation;
    }
}
}