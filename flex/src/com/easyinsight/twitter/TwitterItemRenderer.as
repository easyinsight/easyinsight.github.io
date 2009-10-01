package com.easyinsight.twitter {

import mx.controls.TextArea;
import mx.core.mx_internal;
import mx.events.FlexEvent;

public class TwitterItemRenderer extends TextArea {

    private var message:Tweet;

    public function TwitterItemRenderer() {
        super();
        //this.maxHeight = 80;
        this.percentWidth = 100;
        setStyle("borderStyle", "none");
        //setStyle("borderThickness", 1);
        editable = false;
        setStyle("backgroundAlpha", 0);
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
    }

    private function onCreation(event:FlexEvent):void {
        var ta_height:uint = 10;

        validateNow();

        for(var i:int=0; i < mx_internal::getTextField().numLines; i++) {
            ta_height += mx_internal::getTextField().getLineMetrics(i).height;
        }
        height = ta_height;
    }



    override public function get data():Object {
        return message;
    }

    override public function set data(obj:Object):void {
        message = obj as Tweet;
        if (message != null) {
            this.htmlText = message.status;
        }
    }
}
}