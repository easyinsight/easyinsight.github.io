package com.easyinsight.datasources {
import com.easyinsight.customupload.FileFeedUpdateWindow;
import com.easyinsight.framework.User;
import com.easyinsight.solutions.DataSourceDescriptor;
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.Form;
import mx.containers.FormItem;
import mx.containers.VBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.formatters.DateFormatter;
import mx.managers.PopUpManager;

[Event(name="dataSourceRefreshed", type="com.easyinsight.datasources.DataSourceRefreshEvent")]
public class MyDataDataSourceDisplay extends VBox {

    private var _dataSource:DataSourceDescriptor;

    private var _labelText:String;

    public function MyDataDataSourceDisplay() {
        super();
        setStyle("horizontalAlign", "center");
        setStyle("paddingLeft", 5);
        setStyle("paddingRight", 5);
        setStyle("paddingTop", 5);
        setStyle("paddingBottom", 5);
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

    public function get dataSource():DataSourceDescriptor {
        return _dataSource;
    }

    public function set dataSource(value:DataSourceDescriptor):void {
        if (dataSource != value) {
            _dataSource = value;
            if (_dataSource != null) {
                updateString(value.lastDataTime);
            }
            dataSourceChanged = true;
            invalidateDisplayList();
        }

    }

    private function updateValue(date:Date):String {
        var dateFormatter:DateFormatter = new DateFormatter();
        if (User.getInstance() != null) {
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
        } else {
            dateFormatter.formatString = "YYYY-MM-DD HH:NN";
        }
        return dateFormatter.format(date);
    }

    private function updateString(date:Date):void {
        labelText = updateValue(date);
    }

    private var form:Form;

    private var button:Button;

    override protected function createChildren():void {
        super.createChildren();
        if (form == null) {
            form = new Form();
            var lastDataItem:FormItem = new FormItem();
            lastDataItem.label = "Last Data Time:";
            var lastDataLabel:Label = new Label();
            BindingUtils.bindProperty(lastDataLabel, "text", this, "labelText");
            lastDataItem.addChild(lastDataLabel);
            form.addChild(lastDataItem);
            form.visible = false;
        }
        addChild(form);
        if (button == null) {
            button = new Button();
            button.label = "Refresh the data source";
            button.styleName = "grayButton";
            button.addEventListener(MouseEvent.CLICK, onClick);
            button.visible = false;
        }
        addChild(button);
    }

    private var dataSourceChanged:Boolean = false;

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (dataSourceChanged) {
            if (DataSourceBehavior.pullDataSource(dataSource.dataSourceType)) {
                if (dataSource.lastDataTime != null) {
                    updateString(dataSource.lastDataTime);
                }
                form.visible = true;
                button.visible = true;
            } else {
                form.visible = false;
                button.visible = false;
            }
            dataSourceChanged = false;
        }
    }

    private function onClick(event:MouseEvent):void {
        if (dataSource.dataSourceType == DataSourceType.STATIC ||
                dataSource.dataSourceType == DataSourceType.EMPTY) {
            fileData(dataSource);
        } else {
            refreshData(dataSource);
        }
    }

    private function refreshData(feedDescriptor:DataSourceDescriptor):void {
        var dsRefreshWindow:DataSourceRefreshWindow = new DataSourceRefreshWindow();
        dsRefreshWindow.dataSourceID = feedDescriptor.id;
        dsRefreshWindow.addEventListener(DataSourceRefreshEvent.DATA_SOURCE_REFRESH, onRefresh, false, 0, true);
        PopUpManager.addPopUp(dsRefreshWindow, this, true);
        PopUpUtil.centerPopUp(dsRefreshWindow);
    }

    private function fileData(feedDescriptor:DataSourceDescriptor):void {
        var feedUpdateWindow:FileFeedUpdateWindow = FileFeedUpdateWindow(PopUpManager.createPopUp(this.parent.parent.parent, FileFeedUpdateWindow, true));
        feedUpdateWindow.feedID = feedDescriptor.id;
        PopUpUtil.centerPopUp(feedUpdateWindow);

    }

    private function onRefresh(event:DataSourceRefreshEvent):void {
        updateString(event.newDateTime);
    }
}
}