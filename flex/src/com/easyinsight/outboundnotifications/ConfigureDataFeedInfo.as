package com.easyinsight.outboundnotifications {
import com.easyinsight.customupload.ConfigureDataSource;

import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.managers.PopUpManager;

[Bindable]
[RemoteClass(alias="com.easyinsight.outboundnotifications.ConfigureDataFeedInfo")]
public class ConfigureDataFeedInfo extends TodoEventInfo {

    public var feedName:String;
    public var feedID:int;

    public function ConfigureDataFeedInfo() {
        _canDelete = true;
    }

    override public function getTitle():String {
        return "Configure " + feedName + " to match with your service's settings.";
    }
    override public function onNavigateClick(event:MouseEvent):void {
        super.onNavigateClick(event);
        var configWindow:ConfigureDataSource = new ConfigureDataSource();
        configWindow.dataSourceID = feedID;
        PopUpManager.addPopUp(configWindow, displayObject.parent, true);
        PopUpUtil.centerPopUp(configWindow);

    }

}
}