package com.easyinsight.datasources {
import com.easyinsight.customupload.BaseCampDataSourceCreation;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.basecamp.BaseCampCompositeSource")]
public class BaseCampDataSource extends CompositeServerDataSource {

    public var url:String;

    public function BaseCampDataSource() {
        super();
        this.feedName = "BaseCamp";
    }


    override public function configClass():Class {
        return BaseCampDataSourceCreation;
    }
}
}