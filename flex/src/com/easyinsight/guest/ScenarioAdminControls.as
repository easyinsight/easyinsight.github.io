package com.easyinsight.guest {
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class ScenarioAdminControls extends HBox {

    [Bindable]
    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    private var scenario:Scenario;

    public function ScenarioAdminControls() {
        super();
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
    }

    protected override function createChildren():void {
        super.createChildren();
        var editButton:Button = new Button();
        editButton.setStyle("icon", editIcon);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        addChild(editButton);
    }

    private function onEdit(event:MouseEvent):void {
        var window:ScenarioEditWindow = new ScenarioEditWindow();
        window.scenario = scenario;
        window.addEventListener("refresh", onRefresh);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onRefresh(event:Event):void {
        dispatchEvent(event);
    }

    override public function set data(val:Object):void {
        scenario = val as Scenario;
    }

    override public function get data():Object {
        return scenario;
    }
}
}