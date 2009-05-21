package com.easyinsight.analysis {
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.core.UIComponent;
import mx.events.FlexEvent;
import mx.states.AddChild;
import mx.states.State;

public class DropArea2 extends HBox {

    private var _analysisItem:AnalysisItem;
    private var _selected:Boolean;

    private var editButton:Button;

    private var coreContent:UIComponent;

    private var _analysisItems:ArrayCollection;

    private var configured:State;

    public function DropArea2() {
        super();
        addEventListener(MouseEvent.CLICK, onClick);
        var emptyLabel:EmptyDropAreaLabel = new EmptyDropAreaLabel();
        emptyLabel.text = getNoDataLabel();
        editButton = new Button();
        editButton.label = "...";
        editButton.addEventListener(MouseEvent.CLICK, editEvent);
        editButton.visible = false;
        configured = new State();
        configured.name = "Configured";
        var addChild:AddChild = new AddChild();
        addChild.relativeTo = this;
        addChild.target = editButton;
        configured.overrides = [ addChild ];
        states = [ configured ];
    }

    private function onClick(event:MouseEvent):void {
        selected = !selected;
    }

    protected function getNoDataLabel():String {
        return null;
    }

    private function onConfigured():void {
        currentState = "Configured";
    }

    private function editEvent(event:MouseEvent):void {
        
    }


    [Bindable]
    public function get selected():Boolean {
        return _selected;
    }

    public function set selected(val:Boolean):void {
        _selected = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    private function applySelectedStyle():void {

    }

    private function applyUnselectedStyle():void {
        
    }
}
}