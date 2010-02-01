package com.easyinsight.scorecard {
import flash.display.DisplayObject;
import flash.events.MouseEvent;

import flexlib.containers.FlowBox;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.core.UIComponent;
import mx.events.FlexEvent;
import mx.rpc.remoting.RemoteObject;

[Event(name="scorecardFocus", type="com.easyinsight.scorecard.ScorecardEvent")]
public class ScorecardTabsRenderer extends FlowBox {

    private var _scorecards:ArrayCollection;
    private var scorecardService:RemoteObject;

    public function ScorecardTabsRenderer() {
        super();
        setStyle("horizontalAlign", "center");
        percentWidth = 100;
        addEventListener(FlexEvent.UPDATE_COMPLETE, adapterFlowBoxUpdateCompleteHandler);
        scorecardService = new RemoteObject();
        scorecardService.destination = "scorecardService";
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


    public function set scorecards(value:ArrayCollection):void {
        _scorecards = value;
        invalidateProperties();
    }

    protected override function commitProperties():void {
        super.commitProperties();
        removeAllChildren();
        if (_scorecards != null && _scorecards.length > 0) {
            for each (var scorecard:Scorecard in _scorecards) {
                var scorecardTab:ScorecardTab = new ScorecardTab();
                scorecardTab.scorecard = scorecard;
                scorecardTab.addEventListener(MouseEvent.CLICK, onClick);
                scorecardTab.addEventListener(MoveKPIEvent.KPI_MOVE, onMove);
                addChild(scorecardTab);
            }
            ScorecardTab(getChildAt(0)).selectedTab = true;
        }
    }

    private function onMove(event:MoveKPIEvent):void {
        var selectedScorecard:Scorecard;
        for each (var tab:ScorecardTab in getChildren()) {
            if (tab.selectedTab) {
                selectedScorecard = tab.scorecard;
            }
        }
        selectedScorecard.kpis.removeItemAt(selectedScorecard.kpis.getItemIndex(event.kpi));
        event.destinationScorecard.kpis.addItem(event.kpi);
        scorecardService.removeKPIFromScorecard.send(event.kpi.kpiID, selectedScorecard.scorecardID);
        scorecardService.addKPIToScorecard.send(event.kpi, event.destinationScorecard.scorecardID);
    }

    private function onClick(event:MouseEvent):void {
        for each (var tab:ScorecardTab in getChildren()) {
            tab.selectedTab = false;
        }
        var scorecardTab:ScorecardTab = event.currentTarget as ScorecardTab;
        scorecardTab.selectedTab = true;
        dispatchEvent(new ScorecardEvent(ScorecardEvent.SCORECARD_FOCUS, scorecardTab.scorecard));

    }
}
}