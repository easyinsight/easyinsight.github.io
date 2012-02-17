package com.easyinsight.analysis {
import flash.display.DisplayObject;
import flash.events.Event;

import flexlib.containers.FlowBox;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.core.Container;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class ReportControlBar extends FlowBox {

    private var _analysisItems:ArrayCollection;
    private var _dataSourceID:int;

    public function ReportControlBar() {
        super();
        setStyle("verticalAlign", "middle");
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("borderStyle", "solid");
        setStyle("borderThickness", 1);
        setStyle("paddingLeft", 5);
        setStyle("paddingTop", 5);
        setStyle("paddingRight", 5);
        setStyle("paddingBottom", 5);
        horizontalScrollPolicy = "off";
        verticalScrollPolicy = "off";
        addEventListener(FlexEvent.UPDATE_COMPLETE, adapterFlowBoxUpdateCompleteHandler);
        this.percentWidth = 100;
    }

    public function set dataSourceID(dataSourceID:int):void {
        _dataSourceID = dataSourceID;
    }


    public function get dataSourceID():int {
        return _dataSourceID;
    }

    protected function adapterFlowBoxUpdateCompleteHandler(event:FlexEvent):void
    {
        // resize the FlowBox manually, as the internal calculation doesn't work

        var fb:FlowBox = event.target as FlowBox;

        if (fb != null)
        {
            if (fb.numChildren > 0)
            {
                // default the needed height to the top view metric

                var maxBottom:int = fb.viewMetrics.top;

                // Iterate over the children of the FlowBox to find the bottom-most bottom, so
                // we can determine how big / small we can make the FlowBox.
                // If it's a UIComponent, include it if the includeInLayout property is true.
                // If it's not a UIComponent, include it if the visible property is true.

                for (var idx:int = 0; idx < fb.numChildren; idx++)
                {
                    var displayObject:DisplayObject = fb.getChildAt(idx);

                    if ((displayObject is UIComponent && (displayObject as
                                                          UIComponent).includeInLayout) ||
                        (!(displayObject is UIComponent) && displayObject.visible))
                    {
                        var thisBottom:int = fb.getChildAt(idx).y + fb.getChildAt(idx).height;

                        if (thisBottom > maxBottom)
                            maxBottom = thisBottom;
                    }
                }

                fb.height = maxBottom + fb.viewMetrics.bottom +
                            fb.getStyle("paddingBottom") + 1;
            }
            else
            {
                fb.height = fb.viewMetrics.top + fb.viewMetrics.bottom;
            }
        }
    }

    protected function addDropAreaGrouping(grouping:ListDropAreaGrouping, parent:Container = null):void {
        BindingUtils.bindProperty(grouping, "analysisItems", this, "analysisItems");
        grouping.dataSourceID = _dataSourceID;
        if (parent == null) {
            addChild(grouping);
        } else {
            parent.addChild(grouping);
        }
        invalidateProperties();
    }

    protected override function commitProperties():void {
        super.commitProperties();
        var lastGrouping:ListDropAreaGrouping = null;
        for each (var child:DisplayObject in getChildren()) {
            if (child is ListDropAreaGrouping) {
                lastGrouping = child as ListDropAreaGrouping;
            }
        }
        if (lastGrouping != null) {
            lastGrouping.percentWidth = 100;
        }
    }

    [Bindable(event="analysisItemsChanged")]
    public function get analysisItems():ArrayCollection {
        return _analysisItems;
    }

    public function set analysisItems(value:ArrayCollection):void {
        if (_analysisItems == value) return;
        _analysisItems = value;
        dispatchEvent(new Event("analysisItemsChanged"));
    }

    public function highlight(analysisItem:AnalysisItem):void {
        for each (var child:DisplayObject in getChildren()) {
            if (child is ListDropAreaGrouping) {
                ListDropAreaGrouping(child).highlight(analysisItem);
            }
        }
    }

    public function normal():void {
        for each (var child:DisplayObject in getChildren()) {
            if (child is ListDropAreaGrouping) {
                ListDropAreaGrouping(child).normal();
            }
        }
    }
}
}