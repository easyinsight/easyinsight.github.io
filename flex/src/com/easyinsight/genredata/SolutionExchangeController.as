package com.easyinsight.genredata {
import com.easyinsight.solutions.Solution;

import flash.events.Event;

import mx.collections.ArrayCollection;

import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class SolutionExchangeController extends ExchangeController{

    private var exchangeService:RemoteObject;
    private var _solution:Object;

    public function SolutionExchangeController() {
        super();
        exchangeService = new RemoteObject();
        exchangeService.destination = "solutionService";
        exchangeService.getSolutionReports.addEventListener(ResultEvent.RESULT, onData);
        exchangeService.getInstalledConnections.addEventListener(ResultEvent.RESULT, gotInstalledConnections);
    }

    private var _initializedData:Boolean = false;
    
    override public function initBehavior():void {
        if (!_initializedData) {
            _initializedData = true;
            exchangeService.getInstalledConnections.send();
        }
    }

    private var installedConnections:Object;

    private function gotInstalledConnections(event:ResultEvent):void {
        var connections:ArrayCollection = exchangeService.getInstalledConnections.lastResult as ArrayCollection;
        installedConnections = new Object();
        for each (var solutionID:int in connections) {
            installedConnections[String(solutionID)] = solutionID;
        }
    }

    override public function get text():String {
        return "This page enables you to take advantage of reports built by the community and apply them to your own connection data sources.";
    }

    override public function set subTopicID(value:int):void {
        super.subTopicID = value;
    }

    [Bindable(event="solutionChanged")]
    public function get solution():Object {
        return _solution;
    }

    public function set solution(value:Object):void {
        if (_solution == value) return;
        _solution = value;
        dataProvider2.refresh();
        //dispatchEvent(new Event("updateURL"));
        dispatchEvent(new Event("solutionChanged"));
    }

    private function onData(event:ResultEvent):void {
        var reports:ArrayCollection = exchangeService.getSolutionReports.lastResult as ArrayCollection;
        dispatchEvent(new ExchangeDataEvent(reports));        
    }

    override protected function createExchangedGridPage():ExchangePage {
        return new SolutionExchangeGridPage();
    }

    override protected function retrieveData():void {
        exchangeService.getSolutionReports.send();
    }

    override public function decorateObject(fragmentObject:Object):void {
        if (solution is Solution) {
            fragmentObject.subTopicID = solution.solutionID;
        }
    }

    override protected function filterData(object:Object):Boolean {
        var reportExchangeItem:ExchangeItem = object as ExchangeItem;
        if (solution == null) {
            return false;
        }
        var matched:Boolean = true;
        if (solution is Solution) {
            matched = reportExchangeItem.solutionID == solution.solutionID;
        } else if (solution.name == "[ My Connections ]") {
            matched = installedConnections[String(reportExchangeItem.solutionID)] != null;
        }
        if (matched) {
            if (keyword != null && keyword.length > 0) {
                matched = reportExchangeItem.name.toLowerCase().indexOf(keyword) != -1;
            }
        }
        return matched;
    }
}
}