package com.easyinsight.scorecard {
import com.easyinsight.framework.InsightRequestMetadata;

import flash.display.DisplayObject;
import flash.events.Event;
import flash.events.MouseEvent;

import flexlib.containers.FlowBox;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;
import mx.events.CollectionEvent;
import mx.events.CollectionEventKind;
import mx.events.FlexEvent;
import mx.rpc.remoting.RemoteObject;

[Event(name="scorecardFocus", type="com.easyinsight.scorecard.ScorecardEvent")]
[Event(name="kpiMove", type="com.easyinsight.scorecard.MoveKPIEvent")]
public class ScorecardTabsRenderer extends FlowBox {

    private var _scorecards:ArrayCollection;
    private var scorecardService:RemoteObject;
    private var _scorecard:ScorecardDescriptor;

    public function ScorecardTabsRenderer() {
        super();
        setStyle("horizontalAlign", "center");
        percentWidth = 100;
        addEventListener(FlexEvent.UPDATE_COMPLETE, adapterFlowBoxUpdateCompleteHandler);
        scorecardService = new RemoteObject();
        scorecardService.destination = "scorecardService";
    }


    [Bindable(event="scorecardChanged")]
    public function get scorecard():ScorecardDescriptor {
        return _scorecard;
    }

    public function set scorecard(value:ScorecardDescriptor):void {
        if (_scorecard == value) return;
        _scorecard = value;
        scorecardChanged = true;
        dispatchEvent(new Event("scorecardChanged"));
        invalidateProperties();
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
        if (_scorecards == value) return;
        if (_scorecards != null) {
            _scorecards.removeEventListener(CollectionEvent.COLLECTION_CHANGE, onCollectionChange);
        }
        _scorecards = value;
        if (_scorecards != null) {
            _scorecards.addEventListener(CollectionEvent.COLLECTION_CHANGE, onCollectionChange);
        }
        scorecardsChanged = true;
        invalidateProperties();
    }

    private var scorecardsChanged:Boolean = false;

    private var scorecardChanged:Boolean = false;

    protected override function commitProperties():void {
        super.commitProperties();
        if (scorecardsChanged) {
            removeAllChildren();
            if (_scorecards != null && _scorecards.length > 0) {

                for each (var scorecard:ScorecardDescriptor in _scorecards) {
                    var scorecardTab:ScorecardTab = new ScorecardTab();
                    scorecardTab.scorecard = scorecard;
                    scorecardTab.addEventListener(MouseEvent.CLICK, onClick);
                    scorecardTab.addEventListener(MoveKPIEvent.KPI_MOVE, onMove);
                    addChild(scorecardTab);
                }
                ScorecardTab(getChildAt(0)).selectedTab = true;
            }
        }
        if (scorecardChanged) {
            for each (var tab:ScorecardTab in getChildren()) {
                tab.selectedTab = tab.scorecard == this.scorecard;
            }
        }
    }

    private function onCollectionChange(event:CollectionEvent):void {
        if (event.kind == CollectionEventKind.ADD) {
            var newDescriptor:ScorecardDescriptor = _scorecards.getItemAt(event.location) as ScorecardDescriptor;
            var scorecardTab:ScorecardTab = new ScorecardTab();
            scorecardTab.scorecard = newDescriptor;
            scorecardTab.addEventListener(MouseEvent.CLICK, onClick);
            scorecardTab.addEventListener(MoveKPIEvent.KPI_MOVE, onMove);
            addChild(scorecardTab);
        }
    }

    private function onMove(event:MoveKPIEvent):void {
        var selectedScorecard:ScorecardDescriptor;
        for each (var tab:ScorecardTab in getChildren()) {
            if (tab.selectedTab) {
                selectedScorecard = tab.scorecard;
            }
        }
        //selectedScorecard.kpis.removeItemAt(selectedScorecard.kpis.getItemIndex(event.kpi));
        //event.destinationScorecard.kpis.addItem(event.kpi);
        event.sourceScorecard = selectedScorecard;
        scorecardService.removeKPIFromScorecard.send(event.kpi.kpiID, selectedScorecard.id);
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        scorecardService.addKPIToScorecard.send(event.kpi, event.destinationScorecard.id, metadata);
        dispatchEvent(event);
    }

    private function onClick(event:MouseEvent):void {
        /*for each (var tab:ScorecardTab in getChildren()) {
            tab.selectedTab = false;
        }*/
        var scorecardTab:ScorecardTab = event.currentTarget as ScorecardTab;
        /*scorecardTab.selectedTab = true;
        scorecard = scorecardTab.scorecard;*/
        dispatchEvent(new ScorecardEvent(ScorecardEvent.SCORECARD_FOCUS, scorecardTab.scorecard));

    }
}
}