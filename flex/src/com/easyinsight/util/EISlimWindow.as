package com.easyinsight.util {
import flash.display.Stage;
import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.ui.Keyboard;

import mx.containers.VBox;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

public class EISlimWindow extends VBox {

    private var eiStage:Stage;

    public function EISlimWindow() {
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
        addEventListener(Event.REMOVED, onRemove);
    }

    private function onRemove(event:Event):void {
        if (eiStage != null) {
            eiStage.removeEventListener(KeyboardEvent.KEY_UP, onKey);
            eiStage = null;
        }
    }

    private function onCreation(event:FlexEvent):void {
        eiStage = stage;
        stage.addEventListener(KeyboardEvent.KEY_UP, onKey, false, 0, true);
    }

    private function onKey(event:KeyboardEvent):void {
        if (event.keyCode == Keyboard.ESCAPE) {
            PopUpManager.removePopUp(this);
        }
    }
}
}