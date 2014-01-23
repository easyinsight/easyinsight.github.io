package com.easyinsight.analysis {
import flash.display.DisplayObject;
import flash.events.Event;
import flash.events.MouseEvent;

import flexlib.containers.FlowBox;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.core.Container;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class ReportControlBar extends FlowBox {

    private var _analysisItems:ArrayCollection;
    private var _dataSourceFields:ArrayCollection;

    private var _dataSourceID:int;

    public function ReportControlBar() {
        super();
        horizontalScrollPolicy = "off";
        verticalScrollPolicy = "off";
        addEventListener(FlexEvent.UPDATE_COMPLETE, adapterFlowBoxUpdateCompleteHandler);
        this.percentWidth = 100;
    }

    private var explainButton:Button;

    public function createExplainButton():void {
        if (explainButton == null) {
            explainButton = new Button();
            explainButton.label = "Explain";
            explainButton.addEventListener(MouseEvent.CLICK, onExplain);
            addChildAt(explainButton, 0);
        }
    }

    public function deleteExplainButton():void {
        if (explainButton != null) {
            explainButton.removeEventListener(MouseEvent.CLICK, onExplain);
            removeChild(explainButton);
            explainButton = null;
        }
    }

    private function onExplain(event:MouseEvent):void {
        dispatchEvent(new Event("explainReport", true));
    }

    public function set dataSourceID(dataSourceID:int):void {
        _dataSourceID = dataSourceID;
    }


    public function get dataSourceID():int {
        return _dataSourceID;
    }

    [Bindable(event="dataSourceFieldsChanged")]
    public function get dataSourceFields():ArrayCollection {
        return _dataSourceFields;
    }

    public function set dataSourceFields(value:ArrayCollection):void {
        if (_dataSourceFields == value) return;
        _dataSourceFields = value;
        dispatchEvent(new Event("dataSourceFieldsChanged"));
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
        //BindingUtils.bindProperty(grouping, "dataSourceFields", this, "dataSourceFields");
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

    public function highlight(analysisItem:AnalysisItem):Boolean {
        var valid:Boolean = false;
        for each (var child:DisplayObject in getChildren()) {
            if (child is ListDropAreaGrouping) {
                valid = ListDropAreaGrouping(child).highlight(analysisItem) || valid;
            }
        }
        return valid;
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