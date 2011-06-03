package com.easyinsight.datasources {
import com.easyinsight.analysis.IRetrievable;
import com.easyinsight.framework.User;
import com.easyinsight.util.AutoSizeTextArea;
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.Box;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.containers.ViewStack;
import mx.controls.Button;
import mx.controls.TextArea;
import mx.formatters.DateFormatter;
import mx.managers.PopUpManager;

public class DataSourceDisplay extends VBox {

    private var _dataSource:DataSourceInfo;
    public var sourceLabel:TextArea;

    private var _labelText:String;
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
            switch (User.getInstance().dateFormat) {
                case 0:
                    dateFormatter.formatString = "MM/DD/YYYY HH:NN";
                    break;
                case 1:
                    dateFormatter.formatString = "YYYY-MM-DD HH:NN";
                    break;
                case 2:
                    dateFormatter.formatString = "DD-MM-YYYY HH:NN";
                    break;
                case 3:
                    dateFormatter.formatString = "DD/MM/YYYY HH:NN";
                    break;
                case 4:
                    dateFormatter.formatString = "DD.MM.YYYY HH:NN";
                    break;
            }
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
        button.styleName = "grayButton";
        button.addEventListener(MouseEvent.CLICK, onClick);
        outOfDateBox.addChild(button);

        viewStack.addChild(emptyBox);
        viewStack.addChild(outOfDateBox);

        addChild(viewStack);
        BindingUtils.bindProperty(viewStack, "selectedIndex", this, "stackIndex");
    }

    private function onClick(event:MouseEvent):void {
        var dsRefreshWindow:DataSourceRefreshWindow = new DataSourceRefreshWindow();
        dsRefreshWindow.dataSourceID = _dataSource.dataSourceID;
        PopUpManager.addPopUp(dsRefreshWindow, this, true);
        PopUpUtil.centerPopUp(dsRefreshWindow);
    }
}
}