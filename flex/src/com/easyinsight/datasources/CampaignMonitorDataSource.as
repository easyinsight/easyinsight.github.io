package com.easyinsight.datasources {
import com.easyinsight.customupload.CampaignMonitorDataSourceCreation;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.campaignmonitor.CampaignMonitorDataSource")]
public class CampaignMonitorDataSource extends CompositeServerDataSource {

    public var url:String;
    public var cmApiKey:String;
    public var cmUserName:String;
    public var cmPassword:String;

    public function CampaignMonitorDataSource() {
        super();
        this.feedName = "Campaign Monitor";
    }

    override public function isLiveData():Boolean {
        return false;
    }

    override public function getFeedType():int {
        return DataSourceType.CAMPAIGN_MONITOR;
    }

    override public function configClass():Class {
        return CampaignMonitorDataSourceCreation;
    }
}
}