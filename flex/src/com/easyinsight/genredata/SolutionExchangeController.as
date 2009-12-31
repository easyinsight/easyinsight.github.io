package com.easyinsight.genredata {
import com.easyinsight.solutions.Solution;

import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.core.ClassFactory;
import mx.core.IFactory;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class SolutionExchangeController extends ExchangeController{

    private var exchangeService:RemoteObject;
    private var _solution:Object;

    private var _solutions:ArrayCollection;

    public function SolutionExchangeController() {
        super();
        exchangeService = new RemoteObject();
        exchangeService.destination = "solutionService";
        exchangeService.getSolutionReports.addEventListener(ResultEvent.RESULT, onData);
        exchangeService.getSolutions.addEventListener(ResultEvent.RESULT, gotSolutions);
    }
    
    override public function initBehavior():void {
        if (_solutions == null) {
            exchangeService.getSolutions.send();
        }
    }

    override public function get text():String {
        return "This page enables you to take advantage of reports built by the community and apply them to your own connection data sources.";
    }

    private function gotSolutions(event:ResultEvent):void {
        var solutions:ArrayCollection = exchangeService.getSolutions.lastResult as ArrayCollection;
        var ph:Object = new Object();
        ph["name"] = "[ All Connections ]";
        var solutionTemp:ArrayCollection = new ArrayCollection();
        for each (var solu:Solution in solutions) {
            if (solu.installable) {
                solutionTemp.addItem(solu);
            }
        }
        this.solutions = solutionTemp;
        this.solutions.addItemAt(ph, 0);
        if (subTopicID > 0) {            
            for each (var solObj:Object in this.solutions) {
                if (solObj is Solution) {
                    var sol:Solution = solObj as Solution;
                    if (sol.solutionID == subTopicID) {
                        solution = sol;
                    }
                }
            }
        }
        if (solution == null) {
            solution = this.solutions.getItemAt(0);
        }
    }

    [Bindable(event="solutionsChanged")]
    public function get solutions():ArrayCollection {
        return _solutions;
    }

    override public function set subTopicID(value:int):void {
        super.subTopicID = value;
    }

    public function set solutions(value:ArrayCollection):void {
        if (_solutions == value) return;
        _solutions = value;
        dispatchEvent(new Event("solutionsChanged"));
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
        var page:SolutionExchangeGridPage = new SolutionExchangeGridPage();
        BindingUtils.bindProperty(page, "solutions", this, "solutions");
        BindingUtils.bindProperty(page, "solution", this, "solution");
        BindingUtils.bindProperty(this, "solution", page, "solution");


        return page;
    }

    override protected function createExchangeSummaryPage():ExchangePage {
        var summary:SolutionExchangeSummaryPage = new SolutionExchangeSummaryPage();
        summary.itemRenderer = summaryItemRenderer();
        BindingUtils.bindProperty(summary, "solutions", this, "solutions");
        BindingUtils.bindProperty(summary, "solution", this, "solution");
        BindingUtils.bindProperty(this, "solution", summary, "solution");


        return summary;
    }

    override protected function summaryItemRenderer():IFactory {
        return new ClassFactory(SolutionExchangeSummaryRenderer2);
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
        var reportExchangeItem:SolutionReportExchangeItem = object as SolutionReportExchangeItem;
        var matched:Boolean = true;
        if (solution is Solution) {
            matched = reportExchangeItem.solutionID == solution.solutionID;
        }
        if (selectedTag != null) {
            matched = reportExchangeItem.tags.getItemIndex(selectedTag) != -1;
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