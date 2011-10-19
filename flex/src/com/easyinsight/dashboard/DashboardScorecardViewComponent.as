package com.easyinsight.dashboard {

import com.easyinsight.scorecard.ScorecardRenderer;

import mx.collections.ArrayCollection;
import mx.containers.Canvas;
import mx.containers.VBox;
import mx.controls.Label;

public class DashboardScorecardViewComponent extends Canvas implements IDashboardViewComponent  {

    public var dashboardScorecard:DashboardScorecard;
    private var scorecardRenderer:ScorecardRenderer;

    public function DashboardScorecardViewComponent() {
        super();
        percentWidth = 100;
        percentHeight = 100;
    }

    /*private function exportReport():void {
        viewFactory.updateExportMetadata();
        var window:ReportExportWindow = new ReportExportWindow();
        window.report = viewFactory.report;
        window.coreView = viewFactory.getChildAt(0);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }*/

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo();
    }

    protected override function createChildren():void {
        super.createChildren();

        scorecardRenderer = new ScorecardRenderer();
        scorecardRenderer.scorecardID = dashboardScorecard.scorecard.id;

        if (dashboardScorecard.showLabel) {
            var vbox:VBox = new VBox();
            vbox.percentHeight = 100;
            vbox.percentWidth = 100;
            vbox.setStyle("horizontalAlign", "center");
            addChild(vbox);
            var label:Label = new Label();
            label.text = dashboardScorecard.scorecard.name;
            vbox.addChild(label);
            vbox.addChild(scorecardRenderer);
        } else {
            addChild(scorecardRenderer);
        }
    }

    public function refresh():void {
        scorecardRenderer.refreshValues();
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
        scorecardRenderer.refreshValues();
    }

    public function updateAdditionalFilters(filters:Object):void {
    }

    public function activeStatus(status:Boolean):void {
    }

    public function initialRetrieve():void {
    }

    public function reportCount():ArrayCollection {
        return new ArrayCollection();
    }

    public function toggleFilters(showFilters:Boolean):void {
    }
}
}