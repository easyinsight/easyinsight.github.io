package com.easyinsight.analysis {
import com.easyinsight.framework.User;
import com.easyinsight.report.ReportMetrics;
import com.easyinsight.util.ProgressAlert;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Image;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class ReportRating extends HBox{

    [Bindable]
    [Embed(source="../../../../assets/star_grey.png")]
    private var grayStar:Class;

    [Bindable]
    [Embed(source="../../../../assets/star_green.png")]
    private var greenStar:Class;

    private var stars:Array;

    private var _score:int;

    private var _reportID:int;

    private var analysisService:RemoteObject;

    private var _connection:Boolean = false;

    private var _rateable:Boolean = false;                                                                                  

    public function ReportRating() {
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
            analysisService = new RemoteObject();
            analysisService.destination = "analysisDefinition";
            analysisService.rateReport.addEventListener(ResultEvent.RESULT, onResult);
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
        var metrics:ReportMetrics = analysisService.rateReport.lastResult as ReportMetrics;
        _score = metrics.average;
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
        if (User.getInstance() != null) {
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
            ProgressAlert.alert(this, "Saving your rating...", null, analysisService.rateReport);
            analysisService.rateReport.send(_reportID, _score);
        }
    }
}
}