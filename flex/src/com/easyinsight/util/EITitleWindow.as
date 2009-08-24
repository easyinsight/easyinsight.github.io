package com.easyinsight.util {
import flash.display.Stage;
import flash.events.Event;
import flash.events.KeyboardEvent;

import flash.ui.Keyboard;

import mx.containers.TitleWindow;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

public class EITitleWindow extends TitleWindow{

    public static var windowCount:int = 0;

    private var eiStage:Stage;

    public function EITitleWindow() {
        super();
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
        addEventListener(Event.REMOVED, onRemove);

    }

    private function onRemove(event:Event):void {
        windowCount--;
        if (eiStage != null) {
            eiStage.removeEventListener(KeyboardEvent.KEY_UP, onKey);
            eiStage = null;
        }
    }

    private function onCreation(event:FlexEvent):void {
        windowCount++;
        eiStage = stage;
        stage.addEventListener(KeyboardEvent.KEY_UP, onKey);
    }

    private function onKey(event:KeyboardEvent):void {
        if (event.keyCode == Keyboard.ESCAPE) {
            PopUpManager.removePopUp(this);
        }
    }
}
}