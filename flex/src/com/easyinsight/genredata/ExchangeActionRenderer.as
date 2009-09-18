package com.easyinsight.genredata {
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class ExchangeActionRenderer extends HBox{

    [Bindable]
    [Embed(source="../../../../assets/media_play_green.png")]
    private var playIcon:Class;

    private var exchangeItem:ReportExchangeItem;

    private var runButton:Button;

    public function ExchangeActionRenderer() {
        super();
        setStyle("horizontalAlign", "center");
        percentWidth = 100;
        runButton = new Button();
        runButton.setStyle("icon", playIcon);
        runButton.addEventListener(MouseEvent.CLICK, viewReport);
    }

    private function viewReport(event:MouseEvent):void {
        var insightDescriptor:InsightDescriptor = new InsightDescriptor();
        insightDescriptor.id = exchangeItem.id;
        insightDescriptor.name = exchangeItem.name;
        insightDescriptor.dataFeedID = exchangeItem.dataSourceID;
        insightDescriptor.reportType = exchangeItem.reportType;
        dispatchEvent(new ModuleAnalyzeEvent(new ReportAnalyzeSource(insightDescriptor)));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(runButton);
    }

    override public function set data(val:Object):void {
        exchangeItem = val as ReportExchangeItem;
    }

    override public function get data():Object {
        return exchangeItem;
    }
}
}