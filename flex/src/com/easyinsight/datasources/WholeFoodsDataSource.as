package com.easyinsight.datasources {
import com.easyinsight.customupload.WholeFoodsDataSourceCreation;

import flash.utils.ByteArray;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.wholefoods.WholeFoodsSource")]
public class WholeFoodsDataSource extends CompositeServerDataSource {

    public var initialized:Boolean = false;
    public var wfUserName:String;
    public var wfPassword:String;
    public var startFile:ByteArray;

    public function WholeFoodsDataSource() {
        super();
        this.feedName = "Whole Foods";
        this.uncheckedAPIEnabled = true;
    }

    override public function getFeedType():int {
        return DataSourceType.WHOLE_FOODS;
    }

    override public function configClass():Class {
        return WholeFoodsDataSourceCreation;
    }
}
}