package com.easyinsight.datasources {
import com.easyinsight.analysis.IRetrievable;
import com.easyinsight.framework.CredentialsCache;
import com.easyinsight.scorecard.DataSourceAsyncEvent;
import com.easyinsight.util.AutoSizeTextArea;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.containers.ViewStack;

import mx.controls.Button;
import mx.controls.Label;
import mx.controls.ProgressBar;
import mx.controls.TextArea;
import mx.formatters.DateFormatter;
import mx.messaging.Consumer;
import mx.messaging.events.MessageEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DataSourceDisplay extends VBox {

    private var _dataSource:DataSourceInfo;
    private var sourceLabel:TextArea;

    private var _labelText:String;

    private var consumer:Consumer;

    private var feedService:RemoteObject;

    public function DataSourceDisplay() {
        super();
        setStyle("horizontalAlign", "center");
    }

    private var _dataView:IRetrievable;

    public function set dataView(value:IRetrievable):void {
        _dataView = value;
    }

    [Bindable(event="labelTextChanged")]
    public function get labelText():String {
        return _labelText;
    }

    public function set labelText(value:String):void {
        if (_labelText == value) return;
        _labelText = value;
        dispatchEvent(new Event("labelTextChanged"));
    }

    public function get dataSource():DataSourceInfo {
        return _dataSource;
    }

    public function set dataSource(value:DataSourceInfo):void {
        _dataSource = value;
        if (_dataSource != null) {
            var dateFormatter:DateFormatter = new DateFormatter();
            dateFormatter.formatString = "MM/DD/YYYY HH:NN";
            var dateString:String = dateFormatter.format(value.lastDataTime);
            if (_dataSource.type == DataSourceInfo.STORED_PUSH) {
                if (dataSource.originName == null) {
                    labelText = "This data source is displaying data pushed into Easy Insight at " + dateString + ".";
                } else {
                    labelText = "This data source is displaying data pushed into Easy Insight at " + dateString + " from " + dataSource.originName + ".";
                }
            } else if (_dataSource.type == DataSourceInfo.STORED_PULL || _dataSource.type == DataSourceInfo.COMPOSITE_PULL) {
                if (dataSource.originName == null) {
                    labelText = "This data source is displaying data pulled into Easy Insight at " + dateString + ".";
                } else {
                    labelText = "This data source is displaying data pulled into Easy Insight at " + dateString + " from " + dataSource.originName + ".";
                }
                stackIndex = 1;
            } else if (_dataSource.type == DataSourceInfo.LIVE) {
                if (dataSource.originName == null) {
                    labelText = "This data source is displaying live data as of " + dateString + ".";
                } else {
                    labelText = "This data source is displaying live data as of " + dateString + " from " + dataSource.originName + ".";
                }
            } else if (_dataSource.type == DataSourceInfo.COMPOSITE) {
                labelText = "This data source combines other data sources.";
                for each (var childSource:DataSourceInfo in _dataSource.childSources) {

                }
            } else {
                labelText = "Unknown";
            }
        }

    }

    private var _stackIndex:int;

    [Bindable(event="stackIndexChanged")]
    public function get stackIndex():int {
        return _stackIndex;
    }

    public function set stackIndex(value:int):void {
        if (_stackIndex == value) return;
        _stackIndex = value;
        dispatchEvent(new Event("stackIndexChanged"));
    }

    private var viewStack:ViewStack;

    override protected function createChildren():void {
        super.createChildren();
        /*if (sourceBox == null) {
            sourceBox = new HBox();
        }
        addChild(sourceBox);*/
        if (sourceLabel == null) {
            sourceLabel = new AutoSizeTextArea();
            sourceLabel.width = 250;
            sourceLabel.setStyle("backgroundAlpha", 0);
            sourceLabel.setStyle("borderThickness", 0);
            sourceLabel.editable = false;
            sourceLabel.selectable = false;
            BindingUtils.bindProperty(sourceLabel, "text", this, "labelText");
        }
        addChild(sourceLabel);
        viewStack = new ViewStack();
        viewStack.resizeToContent = true;
        var emptyBox:Box = new Box();

        var outOfDateBox:HBox = new HBox();
        outOfDateBox.percentWidth = 100;
        outOfDateBox.setStyle("horizontalAlign", "center");
        var button:Button = new Button();
        button.label = "Refresh the data source";
        button.addEventListener(MouseEvent.CLICK, onClick);
        outOfDateBox.addChild(button);
        var asyncBox:Box = new Box();
        var progressBar:ProgressBar = new ProgressBar();
        progressBar.indeterminate = true;
        BindingUtils.bindProperty(progressBar, "label", this, "asyncLabel");
        asyncBox.addChild(progressBar);
        var doneBox:HBox = new HBox();
        var doneLabel:Label = new Label();
        doneLabel.text = "New data is available:";
        var refreshButton:Button = new Button();
        refreshButton.addEventListener(MouseEvent.CLICK, refreshReport);
        refreshButton.label = "Refresh Report";
        doneBox.addChild(doneLabel);
        doneBox.addChild(refreshButton);
        viewStack.addChild(emptyBox);
        viewStack.addChild(outOfDateBox);
        viewStack.addChild(asyncBox);
        viewStack.addChild(doneBox);
        addChild(viewStack);
        BindingUtils.bindProperty(viewStack, "selectedIndex", this, "stackIndex");
    }

    private function refreshReport(event:MouseEvent):void {
        _dataView.retrieveData();
        stackIndex = 1;
    }

    private function createConsumer():void {
        consumer = new Consumer();
        consumer.destination = "generalNotifications";
        consumer.addEventListener(MessageEvent.MESSAGE, onMessage);
        consumer.subscribe();
    }

    private function onMessage(event:MessageEvent):void {
        if (event.message.body is DataSourceAsyncEvent) {
            var scorecardEvent:DataSourceAsyncEvent = event.message.body as DataSourceAsyncEvent;
            if (scorecardEvent.dataSourceID == _dataSource.dataSourceID) {
                if (scorecardEvent.type == DataSourceAsyncEvent.NAME_UPDATE) {
                    asyncLabel = "Synchronizing with the latest data from " + scorecardEvent.dataSourceName + "...";
                } else {
                    stackIndex = 3;
                    consumer.removeEventListener(MessageEvent.MESSAGE, onMessage);
                    consumer.disconnect();
                    consumer = null; 
                }
            }
        }
    }

    [Bindable(event="asyncLabelChanged")]
    public function get asyncLabel():String {
        return _asyncLabel;
    }

    public function set asyncLabel(value:String):void {
        if (_asyncLabel == value) return;
        _asyncLabel = value;
        dispatchEvent(new Event("asyncLabelChanged"));
    }

    private var _asyncLabel:String = "Retrieving data source info...";

    private function onClick(event:MouseEvent):void {
        stackIndex = 2;

        feedService = new RemoteObject();
        feedService.destination = "feeds";
        feedService.launchAsyncRefresh.addEventListener(ResultEvent.RESULT, onResult);
        feedService.launchAsyncRefresh.send(_dataSource.dataSourceID, CredentialsCache.getCache().createCredentials());
    }

    private function onResult(event:ResultEvent):void {
        var credentials:ArrayCollection = feedService.launchAsyncRefresh.lastResult as ArrayCollection;
        if (credentials.length == 0) {
            createConsumer();
        } else {
            CredentialsCache.getCache().obtainCredentials(this, credentials, onSuccess);
        }
    }

    private function onSuccess():void {
        feedService.launchAsyncRefresh.send(_dataSource.dataSourceID, CredentialsCache.getCache().createCredentials());
    }
}
}