package com.easyinsight.datasources {
import com.easyinsight.customupload.BaseCampDataSourceCreation;
import com.easyinsight.customupload.WholeFoodsDataSourceCreation;
import com.easyinsight.listing.DataFeedDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.wholefoods.WholeFoodsSource")]
public class WholeFoodsDataSource extends CompositeServerDataSource {

    public var initialized:Boolean = false;
    public var wfUserName:String;
    public var wfPassword:String;

    public function WholeFoodsDataSource() {
        super();
        this.feedName = "Whole Foods";
        this.uncheckedAPIEnabled = true;
    }

    override public function isLiveData():Boolean {
        return false;
    }

    override public function getFeedType():int {
        return DataFeedDescriptor.WHOLE_FOODS;
    }

    override public function configClass():Class {
        return WholeFoodsDataSourceCreation;
    }
}
}