package com.easyinsight.genredata {
import flash.events.Event;

import flash.events.EventDispatcher;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;
import mx.core.IFactory;

[Event(name="changeView", type="com.easyinsight.genredata.ExchangeControllerEvent")]
public class ExchangeController extends EventDispatcher {

    private var _exchangeGridPage:ExchangePage;
    private var _exchangeSummaryPage:ExchangePage;
    private var _exchangeDetailPage:ExchangePage;
    private var _dataProvider:ArrayCollection = new ArrayCollection();
    private var _displayMode:String = "summary";
    private var _selectedTag:String;
    private var _keyword:String;
    private var _selectedPage:ExchangePage;

    public function ExchangeController() {
        addEventListener(ExchangeDataEvent.EXCHANGE_DATA_RETURNED, onDataReturned);
    }

    public function get selectedPage():ExchangePage {
        if (_selectedPage == null) {
            _selectedPage = exchangeSummaryPage;
        }
        return _selectedPage;
    }

    [Bindable(event="displayModeChanged")]
    [Inspectable(category="General", enumeration="grid,summary,detail", defaultValue="summary")]
    public function get displayMode():String {
        return _displayMode;
    }

    public function set displayMode(value:String):void {

        if (_displayMode == value) return;
        _displayMode = value;
        if (_displayMode == "grid") {
            _selectedPage = exchangeGridPage;
            dispatchEvent(new ExchangeControllerEvent(exchangeGridPage));
        } else if (_displayMode == "summary") {
            _selectedPage = exchangeSummaryPage;
            dispatchEvent(new ExchangeControllerEvent(exchangeSummaryPage));
        } else if (_displayMode == "detail") {
            _selectedPage = exchangeDetailPage;
            dispatchEvent(new ExchangeControllerEvent(exchangeDetailPage));
        }
        dispatchEvent(new Event("displayModeChanged"));
    }


    [Bindable(event="selectedTagChanged")]
    public function get selectedTag():String {
        return _selectedTag;
    }

    public function set selectedTag(value:String):void {
        if (_selectedTag == value) return;
        _selectedTag = value;
        if (dataProvider2 != null) {
            dataProvider2.refresh();
        }
        dispatchEvent(new Event("selectedTagChanged"));
    }

    [Bindable(event="keywordChanged")]
    public function get keyword():String {
        return _keyword;
    }

    public function set keyword(value:String):void {
        if (value != null) {
            value = value.toLowerCase();
        }
        if (_keyword == value) return;
        _keyword = value;
        if (dataProvider2 != null) {
            dataProvider2.refresh();
        }
        dispatchEvent(new Event("keywordChanged"));
    }

    [Bindable(event="dataProviderChanged")]
    public function get dataProvider2():ArrayCollection {
        return _dataProvider;
    }

    public function set dataProvider2(value:ArrayCollection):void {
        if (_dataProvider == value) return;
        _dataProvider = value;
        dispatchEvent(new Event("dataProviderChanged"));
    }

    private function configureExchangePage(exchangePage:ExchangePage):void {
        BindingUtils.bindProperty(exchangePage, "dataProvider2", this, "dataProvider2");
        BindingUtils.bindProperty(exchangePage, "selectedTag", this, "selectedTag");
        BindingUtils.bindProperty(this, "selectedTag", exchangePage, "selectedTag");
        BindingUtils.bindProperty(exchangePage, "keyword", this, "keyword");
    }

    protected function filterData(object:Object):Boolean {
        return false;
    }

    private function onDataReturned(event:ExchangeDataEvent):void {
        var data:ArrayCollection = event.data;
        data.filterFunction = filterData;
        if (dataProvider2.sort == null) {
            var sort:Sort = new Sort();
            var sortField:SortField = new SortField();
            sortField.name = "ratingAverage";
            sortField.descending = true;
            sort.fields = [ sortField ];
            data.sort = sort;
        } else {
            data.sort = dataProvider2.sort;
        }
        data.refresh();
        dataProvider2 = data;
    }

    public function get exchangeGridPage():ExchangePage {
        if (_exchangeGridPage == null) {
            _exchangeGridPage = createExchangedGridPage();
            configureExchangePage(_exchangeGridPage);
        }
        return _exchangeGridPage;
    }

    protected function createExchangedGridPage():ExchangePage {
        return null;
    }

    public function get exchangeSummaryPage():ExchangePage {
        if (_exchangeSummaryPage == null) {
            _exchangeSummaryPage = createExchangeSummaryPage();            
            configureExchangePage(_exchangeSummaryPage);
        }
        return _exchangeSummaryPage;
    }

    protected function createExchangeSummaryPage():ExchangePage {
        return null;
    }

    public function get exchangeDetailPage():ExchangePage {
        if (_exchangeDetailPage == null) {
            _exchangeDetailPage = new ExchangeDetailPage();
            configureExchangePage(_exchangeDetailPage);
        }
        return _exchangeDetailPage;
    }

    public function refreshData():void {
        retrieveData();
    }

    protected function summaryItemRenderer():IFactory {
        return null;
    }

    protected function retrieveData():void {
    }
}
}