package com.easyinsight.schedule {
import com.easyinsight.listing.DataFeedDescriptor;

import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Image;
import mx.controls.LinkButton;
import mx.controls.Text;
import mx.managers.PopUpManager;

public class ReportDeliverySetupLine extends HBox {

    private var _dataSource:DataFeedDescriptor;

    [Embed(source="../../../../assets/sign_warning.png")]
    private var warningIcon:Class;

    public function ReportDeliverySetupLine() {
        super();
        setStyle("verticalAlign", "middle");
    }

    public function set dataSource(value:DataFeedDescriptor):void {
        _dataSource = value;
    }

    protected override function createChildren():void {
        super.createChildren();
        var image:Image = new Image();
        image.source = warningIcon;
        addChild(image);
        var setupText:Text = new Text();
        setupText.text = "Your connection to " + _dataSource.name + " is currently not set up to refresh.";
        setupText.setStyle("fontSize", 16);
        addChild(setupText);
        var button:LinkButton = new LinkButton();
        button.label = "Set up refresh";
        button.setStyle("fontSize", 16);
        button.setStyle("textDecoration", "underline");
        button.addEventListener(MouseEvent.CLICK, onClick);
        addChild(button);
    }

    private function newActivity(event:ScheduleActivityEvent):void {
        dispatchEvent(event);
    }

    private function onClick(event:MouseEvent):void {
        var window:DataSourceScheduleWindow = new DataSourceScheduleWindow();
        window.addEventListener(ScheduleActivityEvent.NEW_ACTIVITY, newActivity, false, 0, true);
        window.dataSource = _dataSource;
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }
}
}