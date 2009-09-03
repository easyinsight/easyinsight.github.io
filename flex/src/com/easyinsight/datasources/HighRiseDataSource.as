package com.easyinsight.datasources {
import com.easyinsight.customupload.HighRiseDataSourceCreation;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.highrise.HighRiseCompositeSource")]
public class HighRiseDataSource extends CompositeServerDataSource {

    public var url:String;

    public function HighRiseDataSource() {
        super();
        this.feedName = "Highrise";
    }

    override public function isLiveData():Boolean {
        return false;
    }

    override public function configClass():Class {
        return HighRiseDataSourceCreation;
    }
}
}