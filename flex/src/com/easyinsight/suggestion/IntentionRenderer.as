/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/17/11
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.util.AutoSizeTextArea;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.Text;
import mx.controls.TextArea;

public class IntentionRenderer extends Text {

    private var suggestion:IntentionSuggestion;

    public function IntentionRenderer() {
        buttonMode = true;
        useHandCursor = true;
        mouseEnabled = true;
        mouseChildren = false;
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new IntentionEvent(IntentionEvent.SUGGESTION_CHOICE, suggestion));
    }

    override protected function createChildren():void {
        super.createChildren();
        styleName = "fallThroughFonts";
        addEventListener(MouseEvent.CLICK, onClick);
    }

    override public function set data(val:Object):void {
        suggestion = val as IntentionSuggestion;
        if (suggestion != null) {
            htmlText = suggestion.description;
        }
    }

    override public function get data():Object {
        return suggestion;
    }
}
}
