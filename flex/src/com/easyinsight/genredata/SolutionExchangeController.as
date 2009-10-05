package com.easyinsight.genredata {
import com.easyinsight.solutions.Solution;

import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.core.ClassFactory;
import mx.core.IFactory;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class SolutionExchangeController extends ExchangeController{

    private var exchangeService:RemoteObject;
    private var _solution:Solution;

    private var _solutions:ArrayCollection;

    public function SolutionExchangeController() {
        super();
        exchangeService = new RemoteObject();
        exchangeService.destination = "solutionService";
        exchangeService.getSolutionReports.addEventListener(ResultEvent.RESULT, onData);
        exchangeService.getSolutions.addEventListener(ResultEvent.RESULT, gotSolutions);
        exchangeService.getSolutions.send();
    }

    private function gotSolutions(event:ResultEvent):void {
        var solutions:ArrayCollection = exchangeService.getSolutions.lastResult as ArrayCollection;
        var ph:Object = new Object();
        ph["name"] = "[ All Solutions ]";
        solutions.addItemAt(ph, 0);
        this.solutions = solutions;
    }

    [Bindable(event="solutionsChanged")]
    public function get solutions():ArrayCollection {
        return _solutions;
    }

    public function set solutions(value:ArrayCollection):void {
        if (_solutions == value) return;
        _solutions = value;
        dispatchEvent(new Event("solutionsChanged"));
    }

    [Bindable(event="solutionChanged")]
    public function get solution():Solution {
        return _solution;
    }

    public function set solution(value:Solution):void {
        if (_solution == value) return;
        _solution = value;
        dataProvider2.refresh();
        dispatchEvent(new Event("solutionChanged"));
    }

    private function onData(event:ResultEvent):void {
        var reports:ArrayCollection = exchangeService.getSolutionReports.lastResult as ArrayCollection;
        dispatchEvent(new ExchangeDataEvent(reports));
    }

    override protected function createExchangedGridPage():ExchangePage {
        var page:SolutionExchangeGridPage = new SolutionExchangeGridPage();
        BindingUtils.bindProperty(this, "solution", page, "solution");
        BindingUtils.bindProperty(page, "solutions", this, "solutions");
        return page;
    }

    override protected function createExchangeSummaryPage():ExchangePage {
        var summary:SolutionExchangeSummaryPage = new SolutionExchangeSummaryPage();
        summary.itemRenderer = summaryItemRenderer();
        BindingUtils.bindProperty(this, "solution", summary, "solution");
        BindingUtils.bindProperty(summary, "solutions", this, "solutions");
        return summary;
    }

    override protected function summaryItemRenderer():IFactory {
        return new ClassFactory(SolutionExchangeSummaryRenderer);
    }

    override protected function retrieveData():void {
        exchangeService.getSolutionReports.send();
    }

    override protected function filterData(object:Object):Boolean {
        var reportExchangeItem:SolutionReportExchangeItem = object as SolutionReportExchangeItem;
        var matched:Boolean = true;
        if (solution != null) {
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