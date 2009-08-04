package com.easyinsight.twitter {
import mx.controls.TextArea;

public class TwitterItemRenderer extends TextArea{

    private var message:Tweet;

    public function TwitterItemRenderer() {
        super();
        this.maxHeight = 80;
        setStyle("borderStyle", "none");
        editable = false;
        setStyle("backgroundAlpha", 0);
    }

    override public function get data():Object {
        return message;
    }

    override public function set data(obj:Object):void {
        message = obj as Tweet;
        if (message != null) {
            this.text = message.status;
        }
    }
}
}