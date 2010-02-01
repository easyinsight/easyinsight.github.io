package com.easyinsight.tour {
import flash.events.MouseEvent;

import mx.containers.VBox;
import mx.controls.Button;
import mx.controls.TextArea;
import mx.managers.PopUpManager;

public class FloatingScreenNote extends VBox {

    private var _textHeight:int;

    public function set textHeight(value:int):void {
        _textHeight = value;
    }

    private var _buttonText:String = "Next";

    public function set buttonText(value:String):void {
        _buttonText = value;
    }

    public function FloatingScreenNote() {
        super();
        setStyle("backgroundColor", 0x99ccff);
        setStyle("backgroundAlpha", 1);
        setStyle("borderStyle", "solid");
        setStyle("borderColor", 0);
        setStyle("dropShadowEnabled", true);
        setStyle("cornerRadius", 5);
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        setStyle("paddingLeft", 5);
        setStyle("paddingRight", 5);
        setStyle("paddingTop", 5);
        setStyle("paddingBottom", 5);        
        textArea = new TextArea();
        textArea.setStyle("borderStyle", "none");
        textArea.editable = false;
        textArea.setStyle("fontSize", 14);
        textArea.selectable = false;
        textArea.setStyle("backgroundAlpha", 0);
        textArea.width = 230;
        nextButton = new Button();
        nextButton.label = "Next";
        nextButton.setStyle("fontSize", 16);
        nextButton.addEventListener(MouseEvent.CLICK, nextNote);
    }

    protected override function commitProperties():void {
        super.commitProperties();
        textArea.height = _textHeight;
        nextButton.label = _buttonText;
    }

    private var textArea:TextArea;
    private var nextButton:Button;

    private var _text:String;

    private function nextNote(event:MouseEvent):void {
        PopUpManager.removePopUp(this);
        dispatchEvent(new NoteEvent(NoteEvent.NEXT_NOTE, null));
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(textArea);
        addChild(nextButton);
    }

    public function set text(value:String):void {
        _text = value;
        textArea.text = _text;
    }
}
}