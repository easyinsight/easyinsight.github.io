package com.easyinsight.schedule {
import mx.controls.TextArea;

public class CursorTextArea extends TextArea {
    public function CursorTextArea() {
        super();
    }

    public function insertTextAtCursor(text:String):void {
        var index:int = textField.caretIndex;
        textField.replaceText(index, index, text);
    }
}
}