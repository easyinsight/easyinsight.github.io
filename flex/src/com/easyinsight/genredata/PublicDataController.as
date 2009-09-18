package com.easyinsight.genredata {
import mx.collections.ArrayCollection;
import mx.core.ClassFactory;
import mx.core.IFactory;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class PublicDataController extends ExchangeController{

    private var exchangeService:RemoteObject;

    public function PublicDataController() {
        super();
        exchangeService = new RemoteObject();
        exchangeService.destination = "exchangeService";
        exchangeService.getReports.addEventListener(ResultEvent.RESULT, onData);
    }

    private function onData(event:ResultEvent):void {
        var reports:ArrayCollection = exchangeService.getReports.lastResult as ArrayCollection;
        dispatchEvent(new ExchangeDataEvent(reports));
    }

    override protected function summaryItemRenderer():IFactory {
        return new ClassFactory(MarketplaceReport);
    }

    override protected function retrieveData():void {
        exchangeService.getReports.send(null);
    }

    override protected function filterData(object:Object):Boolean {
        var reportExchangeItem:ReportExchangeItem = object as ReportExchangeItem;
        var matched:Boolean = true;
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