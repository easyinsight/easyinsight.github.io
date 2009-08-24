package com.easyinsight.util {
import flash.events.Event;
import flash.events.KeyboardEvent;

import flash.ui.Keyboard;

import mx.containers.TitleWindow;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

public class EITitleWindow extends TitleWindow{

    public static var windowCount:int = 0;

    public function EITitleWindow() {
        super();
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
        addEventListener(Event.REMOVED, onRemove);
    }

    private function onRemove(event:Event):void {
        windowCount--;
        stage.removeEventListener(KeyboardEvent.KEY_UP, onKey);
    }

    private function onCreation(event:FlexEvent):void {
        windowCount++;
        stage.addEventListener(KeyboardEvent.KEY_UP, onKey);
    }

    private function onKey(event:KeyboardEvent):void {
        if (event.keyCode == Keyboard.ESCAPE) {
            PopUpManager.removePopUp(this);
        }
    }
}
}