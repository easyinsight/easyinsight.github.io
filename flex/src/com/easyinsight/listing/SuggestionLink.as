/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/16/11
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {

import flash.events.MouseEvent;

import mx.controls.Alert;

import mx.controls.Text;

public class SuggestionLink extends Text {

    private var actionLog:DataSourceSuggestion;

    public function SuggestionLink() {
        styleName = "fallThroughFonts";
        setStyle("color", 0x222222);
        addEventListener(MouseEvent.CLICK, onClick);
        addEventListener(MouseEvent.ROLL_OVER, onRollover);
        addEventListener(MouseEvent.ROLL_OUT, onRollout);
        setStyle("textAlign", "left");
        this.width = 180;
        this.maxWidth = 180;
        useHandCursor = true;
        buttonMode = true;
        mouseEnabled = true;
        mouseChildren = false;
        selectable = false;
    }

    private function onRollover(event:MouseEvent):void {
        setStyle("textDecoration", "underline");
    }

    private function onRollout(event:MouseEvent):void {
        setStyle("textDecoration", "none");
    }

    private function onClick(event:MouseEvent):void {

        dispatchEvent(new DataSourceSuggestionEvent(actionLog.suggestionType));
    }

    override public function set data(val:Object):void {
        actionLog = val as DataSourceSuggestion;
        if (actionLog != null) {
            this.text = actionLog.suggestionLabel;
        }
    }

    override public function get data():Object {
        return actionLog;
    }
}
}
