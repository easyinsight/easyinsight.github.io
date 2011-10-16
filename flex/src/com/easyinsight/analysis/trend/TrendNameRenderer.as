package com.easyinsight.analysis.trend {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.TrendOutcome;
import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.filtering.FilterValueDefinition;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.listing.ReportEditorAnalyzeSource;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.events.ContextMenuEvent;

import flash.events.Event;
import flash.ui.ContextMenuItem;

import mx.collections.ArrayCollection;

import mx.controls.Label;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;

public class TrendNameRenderer extends UIComponent implements IListItemRenderer {

    private var nameLabel:Label;

    public function TrendNameRenderer() {
        super();
        nameLabel = new Label();
        var exploreOption:ContextMenuItem = new ContextMenuItem("Analyze the trend...");
        exploreOption.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, analyzeData);
        options.push(exploreOption);
    }

    private var options:Array = [];

    override protected function createChildren():void {
        super.createChildren();
        addChild(nameLabel);
    }

    private var trendOutcome:TrendOutcome;

    [Bindable("dataChange")]
    public function set data(val:Object):void {
        trendOutcome = val as TrendOutcome;
        if (trendOutcome != null) {
            nameLabel.text = trendOutcome.measure.display;
            var filters:ArrayCollection = trendOutcome.measure.filters;
            if (trendOutcome.dimensions != null) {
                var dimension:AnalysisItem = trendOutcome.report.groupings.getItemAt(0) as AnalysisItem;
                filters = new ArrayCollection(filters.toArray());
                var filterDefinition:FilterValueDefinition = new FilterValueDefinition();
                filterDefinition.field = dimension;
                filterDefinition.singleValue = true;
                filterDefinition.filteredValues = new ArrayCollection([trendOutcome.dimensions[dimension.qualifiedName()]]);
                filterDefinition.enabled = true;
                filterDefinition.inclusive = true;
                filters.addItem(filterDefinition);
            }
            new StandardContextWindow(trendOutcome.measure, passThrough, this, trendOutcome.dimensions, true, filters, trendOutcome.measure.getFormatter().format(trendOutcome.now), options, null);
        }
    }

    private function analyzeData(event:ContextMenuEvent):void {
        var report:ListDefinition = new ListDefinition();
        report.filterDefinitions = trendOutcome.measure.filters;
        report.canSaveDirectly = true;
        report.dataFeedID = trendOutcome.dataSourceID;
        report.columns = new ArrayCollection([ trendOutcome.measure ]);
        report.name = trendOutcome.measure.display;
        dispatchEvent(new AnalyzeEvent(new ReportEditorAnalyzeSource(report)));
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        nameLabel.setActualSize(unscaledWidth, unscaledHeight);
    }

    public function get data():Object {
        return null;
    }
}
}