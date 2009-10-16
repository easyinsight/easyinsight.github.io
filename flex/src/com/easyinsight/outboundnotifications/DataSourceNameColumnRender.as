package com.easyinsight.outboundnotifications {
import com.easyinsight.analysis.UserCapabilities;
import com.easyinsight.genredata.AsyncWindowAnalyzeEvent;


import com.easyinsight.listing.DescriptorAnalyzeSource;

import com.easyinsight.report.MultiReportAnalyzeSource;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.LinkButton;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DataSourceNameColumnRender extends HBox {

    private var infoLabel:LinkButton;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var cancelIcon:Class;

    private var analysisService:RemoteObject;

    [Bindable("dataChange")]
    [Inspectable(environment="none")]

    override public function get data():Object {
        return dataItem;
    }

    override public function set data(value:Object):void {

        this.dataItem = value as RefreshEventInfo;
        this.removeAllChildren();
        infoLabel = new LinkButton();
        infoLabel.label = dataItem.feedName;
        infoLabel.addEventListener(MouseEvent.CLICK, onClick);
        this.addChild(infoLabel);
    }

    private function onClick(event:MouseEvent):void {
        // dispatchEvent(new AsyncWindowAnalyzeEvent(new MultiReportAnalyzeSource(dataItem.feedId, dataItem.feedName)));
        analysisService.getUserCapabilitiesForFeed(dataItem.feedId);
    }

    private var dataItem:RefreshEventInfo;
    

    public function DataSourceNameColumnRender() {
        this.setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        this.setStyle("verticalAlign", "middle");
        analysisService = new RemoteObject();
        analysisService.destination = "analysisDefinition";
        analysisService.getUserCapabilitiesForFeed.addEventListener(ResultEvent.RESULT, onUserCapabilities);
        analysisService.getUserCapabilitiesForFeed.addEventListener(FaultEvent.FAULT, onFault);
    }

    private function onUserCapabilities(event:ResultEvent):void {
        var capabilities:UserCapabilities = event.result as UserCapabilities;
        if(capabilities.reportingAvailable) {
            dispatchEvent(new AsyncWindowAnalyzeEvent(new MultiReportAnalyzeSource(dataItem.feedId, dataItem.feedName)));
        }
        else {
            dispatchEvent(new AsyncWindowAnalyzeEvent(new DescriptorAnalyzeSource(dataItem.feedId, dataItem.feedName)));
        }
    }

    private function onFault(event:FaultEvent):void {
        Alert.show(event.fault.faultDetail);
    }

    override protected function createChildren():void {
        super.createChildren();

    }
}
}