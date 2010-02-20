package com.easyinsight.kpi {
import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.Box;
import mx.containers.Canvas;
import mx.containers.HBox;
import mx.controls.Button;

[Event(name="kpiTutorialNext", type="com.easyinsight.kpi.KPITutorialEvent")]
[Event(name="kpiTutorialPrevious", type="com.easyinsight.kpi.KPITutorialEvent")]
[Event(name="kpiTutorialCancel", type="com.easyinsight.kpi.KPITutorialEvent")]
[Event(name="kpiTutorialFinish", type="com.easyinsight.kpi.KPITutorialEvent")]
[Event(name="expertMode", type="com.easyinsight.kpi.KPIModeEvent")]
public class TutorialButtons extends Canvas {
    public function TutorialButtons() {
        super();
        percentWidth = 100;
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("horizontalAlign", "center");

    }

    private var _backEnabled:Boolean;
    private var _nextEnabled:Boolean;
    private var _finishEnabled:Boolean;

    private var _tutorialPanel:ITutorialPanel;

    public function set tutorialPanel(value:ITutorialPanel):void {
        _tutorialPanel = value;
    }

    private function back(event:MouseEvent):void {
        dispatchEvent(new KPITutorialEvent(KPITutorialEvent.PREVIOUS));
    }

    private function next(event:MouseEvent):void {
        if (_tutorialPanel.validate()) {
            _tutorialPanel.saveValues();
            dispatchEvent(new KPITutorialEvent(KPITutorialEvent.NEXT));
        }
    }

    private function finish(event:MouseEvent):void {
        if (_tutorialPanel.validate()) {
            _tutorialPanel.saveValues();
            dispatchEvent(new KPITutorialEvent(KPITutorialEvent.FINISH));
        }
    }

    private function cancel(event:MouseEvent):void {
        dispatchEvent(new KPITutorialEvent(KPITutorialEvent.CANCEL));
    }

    private function expertMode(event:MouseEvent):void {
        dispatchEvent(new KPIModeEvent(KPIModeEvent.EXPERT_MODE));
    }

    protected override function createChildren():void {
        super.createChildren();
        var expertBox:Box = new Box();
        var expertButton:Button = new Button();
        expertButton.label = "Expert Mode";
        expertButton.setStyle("fontSize", 14);
        expertButton.addEventListener(MouseEvent.CLICK, expertMode);
        expertBox.setStyle("paddingBottom", 8);
        expertBox.setStyle("paddingLeft", 8);
        expertBox.addChild(expertButton);
        addChild(expertBox);
        var hbox:HBox = new HBox();
        hbox.percentWidth = 100;
        hbox.setStyle("horizontalAlign", "center");
        var backButton:Button = new Button();
        backButton.label = "Back";
        backButton.setStyle("fontSize", 14);
        backButton.addEventListener(MouseEvent.CLICK, back);
        BindingUtils.bindProperty(backButton, "enabled", this, "backEnabled");
        hbox.addChild(backButton);
        var nextButton:Button = new Button();
        nextButton.label = "Next";
        nextButton.setStyle("fontSize", 14);
        BindingUtils.bindProperty(nextButton, "enabled", this, "nextEnabled");
        nextButton.addEventListener(MouseEvent.CLICK, next);
        hbox.addChild(nextButton);
        var finishButton:Button = new Button();
        finishButton.label = "Finish";
        finishButton.setStyle("fontSize", 14);
        BindingUtils.bindProperty(finishButton, "enabled", this, "finishEnabled");
        finishButton.addEventListener(MouseEvent.CLICK, finish);
        hbox.addChild(finishButton);
        var cancelButton:Button = new Button();
        cancelButton.label = "Cancel";
        cancelButton.setStyle("fontSize", 14);
        cancelButton.addEventListener(MouseEvent.CLICK, cancel);
        hbox.addChild(cancelButton);
        hbox.setStyle("paddingBottom", 8);
        addChild(hbox);
    }

    [Bindable(event="backEnabledChanged")]
    public function get backEnabled():Boolean {
        return _backEnabled;
    }

    public function set backEnabled(value:Boolean):void {
        if (_backEnabled == value) return;
        _backEnabled = value;
        dispatchEvent(new Event("backEnabledChanged"));
    }

    [Bindable(event="nextEnabledChanged")]
    public function get nextEnabled():Boolean {
        return _nextEnabled;
    }

    public function set nextEnabled(value:Boolean):void {
        if (_nextEnabled == value) return;
        _nextEnabled = value;
        dispatchEvent(new Event("nextEnabledChanged"));
    }

    [Bindable(event="finishEnabledChanged")]
    public function get finishEnabled():Boolean {
        return _finishEnabled;
    }

    public function set finishEnabled(value:Boolean):void {
        if (_finishEnabled == value) return;
        _finishEnabled = value;
        dispatchEvent(new Event("finishEnabledChanged"));
    }
}
}