package com.easyinsight.datasources {
import com.easyinsight.analysis.IRetrievable;
import com.easyinsight.framework.User;
import com.easyinsight.util.AutoSizeTextArea;
import com.easyinsight.util.MultiLineButton;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.Box;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.containers.ViewStack;
import mx.controls.Button;
import mx.controls.TextArea;
import mx.controls.VRule;
import mx.effects.Effect;
import mx.effects.Fade;
import mx.events.FlexEvent;
import mx.formatters.DateFormatter;
import mx.managers.PopUpManager;

[Event(name="dataSourceRefreshed", type="com.easyinsight.datasources.DataSourceRefreshEvent")]
public class DataSourceDisplay extends HBox {

    private var _dataSource:DataSourceInfo;
    public var sourceLabel:TextArea;

    private var _labelText:String;

    public function DataSourceDisplay() {
        super();
        setStyle("verticalAlign", "middle");
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("borderStyle", "inset");
        setStyle("dropShadowEnabled", true);
        setStyle("borderThickness", 1);
        setStyle("cornerRadius", 8);
        setStyle("paddingLeft", 5);
        setStyle("paddingRight", 5);
        setStyle("paddingTop", 5);
        setStyle("paddingBottom", 5);
        setStyle("alpha", 0);
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
    }

    private function onCreation(event:FlexEvent):void {
        stage.addEventListener(MouseEvent.MOUSE_DOWN, onMouseDown, false, 0, true);
        var resizeEffect:Effect = new Fade(this);
        resizeEffect.duration = 500;
        Fade(resizeEffect).alphaFrom = 0;
        Fade(resizeEffect).alphaTo = 1;
        resizeEffect.play();

        var fadeEffect:Effect = new Fade(this);
        fadeEffect.duration = 500;
        Fade(fadeEffect).alphaFrom = 1;
        Fade(fadeEffect).alphaTo = 0;
        setStyle("removedEffect", fadeEffect);
    }

    private function onMouseDown(event:MouseEvent):void {
        var result:Boolean = hitTestPoint(event.stageX, event.stageY);
        if (!result) {
            stage.removeEventListener(MouseEvent.MOUSE_DOWN,  onMouseDown);
            PopUpManager.removePopUp(this);
        }
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
            updateString(value.lastDataTime);
        }

    }

    private function updateString(date:Date):void {
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
        var dateString:String = dateFormatter.format(date);
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

    private function vanillaRetrieval(event:MouseEvent):void {
        _dataView.refresh();
        stage.removeEventListener(MouseEvent.MOUSE_DOWN,  onMouseDown);
        PopUpManager.removePopUp(this);
    }

    override protected function createChildren():void {
        super.createChildren();
        /*if (sourceBox == null) {
            sourceBox = new HBox();
        }
        addChild(sourceBox);*/
        var refreshButton:Button = new MultiLineButton();
        refreshButton.label = "Rerun the Report Against Existing Data";
        refreshButton.styleName = "grayButton";
        refreshButton.width = 120;
        refreshButton.setStyle("fontSize", 12);
        refreshButton.addEventListener(MouseEvent.CLICK, vanillaRetrieval);
        addChild(refreshButton);
        var stroke:VRule = new VRule();
        stroke.height = 60;
        addChild(stroke);
        var vbox:VBox = new VBox();
        vbox.setStyle("horizontalAlign", "center");
        if (sourceLabel == null) {
            sourceLabel = new AutoSizeTextArea();
            sourceLabel.width = 250;
            sourceLabel.setStyle("fontSize", 12);
            sourceLabel.setStyle("backgroundAlpha", 0);
            sourceLabel.setStyle("borderThickness", 0);
            sourceLabel.horizontalScrollPolicy = "off";
            sourceLabel.verticalScrollPolicy = "off";
            sourceLabel.editable = false;
            sourceLabel.selectable = false;
            BindingUtils.bindProperty(sourceLabel, "text", this, "labelText");
        }
        vbox.addChild(sourceLabel);
        viewStack = new ViewStack();
        viewStack.resizeToContent = true;
        var emptyBox:Box = new Box();

        var outOfDateBox:HBox = new HBox();
        outOfDateBox.percentWidth = 100;
        outOfDateBox.setStyle("horizontalAlign", "center");
        var button:Button = new Button();
        button.label = "Refresh the data source";
        button.setStyle("fontSize", 12);
        button.styleName = "grayButton";
        button.addEventListener(MouseEvent.CLICK, onClick);
        outOfDateBox.addChild(button);

        viewStack.addChild(emptyBox);
        viewStack.addChild(outOfDateBox);

        vbox.addChild(viewStack);
        addChild(vbox);
        BindingUtils.bindProperty(viewStack, "selectedIndex", this, "stackIndex");
    }

    private function onClick(event:MouseEvent):void {
        var executor:DataSourceRefreshExecutor = new DataSourceRefreshExecutor();
        executor.addEventListener(DataSourceRefreshEvent.DATA_SOURCE_REFRESH, onRefresh, false, 0, true);
        executor.refresh(dataSource);
    }

    private function onRefresh(event:DataSourceRefreshEvent):void {
        updateString(event.newDateTime);
        stage.removeEventListener(MouseEvent.MOUSE_DOWN,  onMouseDown);
        PopUpManager.removePopUp(this);
    }
}
}