package com.easyinsight.datasources {
import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.controls.Label;
import mx.controls.TileList;
import mx.formatters.DateFormatter;

public class DataSourceDisplay extends HBox {

    private var tileList:TileList;
    private var _dataSource:DataSourceInfo;
    private var sourceLabel:Label;
    private var sourceBox:HBox;

    private var _labelText:String;

    public function DataSourceDisplay() {
        super();
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

    override protected function createChildren():void {
        super.createChildren();
        /*if (sourceBox == null) {
            sourceBox = new HBox();
        }
        addChild(sourceBox);*/
        if (sourceLabel == null) {
            sourceLabel = new Label();
            BindingUtils.bindProperty(sourceLabel, "text", this, "labelText");
        }
        addChild(sourceLabel);
        /*if (tileList == null) {
            tileList = new TileList();
        }
        addChild(tileList);*/
    }
}
}