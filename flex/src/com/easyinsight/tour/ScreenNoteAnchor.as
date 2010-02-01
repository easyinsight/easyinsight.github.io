package com.easyinsight.tour {
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;
import flash.events.Event;

import mx.core.Application;
import mx.managers.PopUpManager;

public class ScreenNoteAnchor extends TutorialElement {

    private var text:String;
    private var position:String;
    private var textHeight:int;
    private var buttonText:String = "Next";

    public function ScreenNoteAnchor(text:String, position:String, height:int, buttonText:String = "Next") {
        super();
        this.text = text;
        this.position = position;
        this.textHeight = height;
        this.buttonText = buttonText;
    }

    override public function staysOnScreen():Boolean {
        return true;
    }

    override public function forwardExecute():void {
        var note:FloatingScreenNote = new FloatingScreenNote();
        note.text = text;
        note.textHeight = textHeight;
        note.buttonText = buttonText;
        PopUpManager.addPopUp(note, DisplayObject(Application.application));
        if (position == "center") {
            PopUpUtil.centerPopUp(note);
        } else {
            note.x = Application.application.width - note.width;
            note.y = 0;
        }
        note.addEventListener(NoteEvent.NEXT_NOTE, passThrough);
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }
}
}