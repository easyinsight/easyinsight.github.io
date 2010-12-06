package com.easyinsight.analysis {

import com.easyinsight.report.ReportMetrics;
import com.easyinsight.util.ProgressAlert;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Image;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

[Event(name="reportRating", type="com.easyinsight.analysis.ReportRatingEvent")]

public class DashboardRating extends HBox{

    [Bindable]
    [Embed(source="../../../../assets/star_grey.png")]
    private var grayStar:Class;

    [Bindable]
    [Embed(source="../../../../assets/star_green.png")]
    private var greenStar:Class;

    private var stars:Array;

    private var _score:int;

    private var _reportID:int;

    private var dashboardService:RemoteObject;

    private var _connection:Boolean = false;

    private var _rateable:Boolean = false;                                                                                  

    public function DashboardRating() {
        super();
    }

    public function set connection(value:Boolean):void {
        _connection = value;
    }

    public function set rateable(value:Boolean):void {
        _rateable = value;
    }

    public function set score(value:int):void {
        _score = value;
        if (stars != null) {
            updateScores();
        }
    }

    public function set reportID(value:int):void {
        _reportID = value;
    }

    override protected function createChildren():void {
        super.createChildren();
        if (_rateable) {
            dashboardService = new RemoteObject();
            dashboardService.destination = "dashboardService";
            dashboardService.rateDashboard.addEventListener(ResultEvent.RESULT, onResult);
        }
        
        var rate1:Image = new Image();
        rate1.source = _score < 1 ? grayStar : greenStar;
        if (_rateable) rate1.addEventListener(MouseEvent.CLICK, onMouseClick);
        addChild(rate1);
        var rate2:Image = new Image();
        rate2.source = _score < 2 ? grayStar : greenStar;
        if (_rateable) rate2.addEventListener(MouseEvent.CLICK, onMouseClick);
        addChild(rate2);
        var rate3:Image = new Image();
        rate3.source = _score < 3 ? grayStar : greenStar;
        if (_rateable) rate3.addEventListener(MouseEvent.CLICK, onMouseClick);
        addChild(rate3);
        var rate4:Image = new Image();
        rate4.source = _score < 4 ? grayStar : greenStar;
        if (_rateable) rate4.addEventListener(MouseEvent.CLICK, onMouseClick);
        addChild(rate4);
        var rate5:Image = new Image();
        rate5.source = _score < 5 ? grayStar : greenStar;
        if (_rateable) rate5.addEventListener(MouseEvent.CLICK, onMouseClick);        
        addChild(rate5);
        stars = [rate1, rate2, rate3, rate4, rate5];
    }

    private function onResult(event:ResultEvent):void {
        var metrics:ReportMetrics = dashboardService.rateDashboard.lastResult as ReportMetrics;
        _score = metrics.myRating;
        dispatchEvent(new ReportRatingEvent(metrics.average));
    }

    private function updateScores():void {
        var i:int = 0;
        for each (var testImage:Image in stars) {
            i++;
            if (i <= _score) {
                testImage.source = greenStar;
            } else {
                testImage.source = grayStar;
            }
        }
    }

    private function onMouseClick(event:MouseEvent):void {
        var image:Image = event.currentTarget as Image;
        var i:int = 0;
        for each (var testImage:Image in stars) {
            //testImage.source = greenStar;
            i++;
            if (testImage == image) {
                break;
            }
        }
        score = i;
        ProgressAlert.alert(this, "Saving your rating...", null, dashboardService.rateDashboard);
        dashboardService.rateDashboard.send(_reportID, _score);
}
}
}