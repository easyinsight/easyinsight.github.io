package com.easyinsight.framework {

import flash.events.MouseEvent;

import mx.containers.Canvas;
import mx.events.FlexEvent;

public class ButtonControlBar extends Canvas{

    private var showingWindow:Boolean;

    private var floatingWindow:FloatingWindowLayout;

    public function ButtonControlBar() {
        super();
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
    }

    private function onCreation(event:FlexEvent):void {
        for each (var button:CorePageButton in getChildren()) {
            button.addEventListener(MouseEvent.CLICK, onClick);
        }
    }

    private function onClick(event:MouseEvent):void {

    }
}
}