package com.easyinsight.datasources {
import com.easyinsight.customupload.BaseCampDataSourceCreation;
import com.easyinsight.customupload.HarvestDataSourceCreation;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.harvest.HarvestCompositeSource")]
public class HarvestDataSource extends CompositeServerDataSource {

    public var url:String;
    public var username:String;
    public var password:String;
    public var accessToken:String;
    public var refreshToken:String;

    public function HarvestDataSource() {
        super();
        this.feedName = "Harvest";
    }

    override public function getFeedType():int {
        return DataSourceType.HARVEST_COMPOSITE;
    }

    override public function configClass():Class {
        return HarvestDataSourceCreation;
    }
}
}