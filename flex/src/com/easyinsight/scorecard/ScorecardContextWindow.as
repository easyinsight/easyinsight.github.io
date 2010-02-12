package com.easyinsight.scorecard {

import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.framework.NavigationEvent;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.kpi.KPI;
import com.easyinsight.listing.ReportEditorAnalyzeSource;
import com.easyinsight.pseudocontext.*;

import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;

import flash.ui.Keyboard;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.VBox;

import mx.controls.Label;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

public class ScorecardContextWindow extends VBox {

    private var items:Array;

    private var kpi:KPI;

    private var passthroughFunction:Function;
    private var passthroughObject:Object;

    private var _contextMenuAvailable:Boolean;

    public function ScorecardContextWindow(kpi:KPI, passthroughFunction:Function, passthroughObject:Object) {
        super();
        this.kpi = kpi;
        this.passthroughFunction = passthroughFunction;
        this.passthroughObject = passthroughObject;
        items = [];
        var copyItem:PseudoContextItem = new PseudoContextItem("Analyze the KPI...", analyzeKPI);
        items.push(copyItem);
        if (kpi.connectionID > 0) {
            var exchangeItem:PseudoContextItem = new PseudoContextItem("Find reports for this KPI...", findReports);
            items.push(exchangeItem);
        }
        if (kpi.reports != null && kpi.reports.length > 0) {
            for each (var report:InsightDescriptor in kpi.reports) {
                var reportContextItem:PseudoContextItem = new PseudoContextItem(report.name, reportClick, report);
                items.push(reportContextItem);
            }
        }

        addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
        setStyle("borderStyle", "solid");
        setStyle("borderThickness", 1);
        setStyle("cornerRadius", 5);
        setStyle("dropShadowEnabled", true);
        this.width = 250;
        setStyle("paddingTop", 10);
        setStyle("paddingBottom", 10);
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("backgroundAlpha", 1);
    }


    [Bindable(event="contextMenuAvailableChanged")]
    public function get contextMenuAvailable():Boolean {
        return _contextMenuAvailable;
    }

    public function set contextMenuAvailable(value:Boolean):void {
        if (_contextMenuAvailable == value) return;
        _contextMenuAvailable = value;
        dispatchEvent(new Event("contextMenuAvailableChanged"));
    }

    private function findReports(event:MouseEvent):void {
        passthroughFunction.call(passthroughObject, new NavigationEvent("Exchange", null, {viewMode: 1, displayMode: 0, subTopicID: kpi.connectionID}));
        destroy();
    }

    private function analyzeKPI(event:MouseEvent):void {
        var report:ListDefinition = new ListDefinition();
        report.filterDefinitions = kpi.filters;
        report.canSaveDirectly = true;
        report.dataFeedID = kpi.coreFeedID;
        report.columns = new ArrayCollection([ kpi.analysisMeasure ]);
        report.name = kpi.name;
        passthroughFunction.call(passthroughObject, new AnalyzeEvent(new ReportEditorAnalyzeSource(report, 0)));
        destroy();
    }


    private function onCreationComplete(event:FlexEvent):void {
        stage.addEventListener(MouseEvent.CLICK, onStageClick);
        stage.addEventListener(KeyboardEvent.KEY_UP, onKeyUp);
    }

    private function onKeyUp(event:KeyboardEvent):void {
        if (event.keyCode == Keyboard.ESCAPE) {
            destroy();
        }
    }

    public function destroy():void {
        stage.removeEventListener(MouseEvent.CLICK, onStageClick);
        stage.removeEventListener(KeyboardEvent.KEY_UP, onKeyUp);
        PopUpManager.removePopUp(this);
    }

    private function onStageClick(event:MouseEvent):void {
        if (!hitTestPoint(event.stageX, event.stageY)) {
            destroy();
        }
    }

    override protected function createChildren():void {
        super.createChildren();

        for (var i:int = 0; i < items.length; i++) {
            var item:PseudoContextItem = items[i];
            var labelBox:Box = new Box();
            labelBox.setStyle("paddingLeft", 10);
            labelBox.setStyle("paddingRight", 10);
            labelBox.setStyle("backgroundColor", 0xFFFFFF);
            labelBox.mouseChildren = false;
            labelBox.percentWidth = 100;
            var label:Label = new Label();
            label.setStyle("fontSize", 13);
            label.text = item.label;
            label.maxWidth = 220;
            labelBox.data = item.data;
            labelBox.addEventListener(MouseEvent.CLICK, item.click);
            labelBox.addEventListener(MouseEvent.MOUSE_OVER, labelMouseOver);
            labelBox.addEventListener(MouseEvent.MOUSE_OUT, labelMouseOut);
            labelBox.addChild(label);
            addChild(labelBox);
        }
    }

    private function labelMouseOver(event:MouseEvent):void {
        var label:Box = event.currentTarget as Box;
        if (label.getStyle("backgroundColor") == 0xFFFFFF) {
            label.setStyle("backgroundColor", 0x3370ce);
        }
    }

    private function labelMouseOut(event:MouseEvent):void {
        var label:Box = event.currentTarget as Box;
        if (label.getStyle("backgroundColor") == 0x3370ce) {
            label.setStyle("backgroundColor", 0xFFFFFF);
        }
    }

    private function reportClick(event:MouseEvent):void {
        destroy();
        passthroughFunction.call(passthroughObject, new AnalyzeEvent(new ReportAnalyzeSource(event.currentTarget.data as InsightDescriptor)));
    }
}
}