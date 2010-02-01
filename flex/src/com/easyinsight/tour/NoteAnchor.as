package com.easyinsight.tour {
import com.easyinsight.util.Callout;

import mx.controls.Alert;
import mx.core.Container;
import mx.core.UIComponent;

[Event(name="nextNote", type="com.easyinsight.tour.NoteEvent")]
[Event(name="previousNote", type="com.easyinsight.tour.NoteEvent")]
[Event(name="closeNote", type="com.easyinsight.tour.NoteEvent")]
public class NoteAnchor extends TutorialElement {
    public function NoteAnchor(anchor:String, text:String, showPrevious:Boolean = true, showNext:Boolean = true, preferredTailPosition:String = null,
            textHeight:uint = 160) {
        this.anchor = anchor;
        this.text = text;
        this.showPrevious = showPrevious;
        this.showNext = showNext;
        this.preferredTailPosition = preferredTailPosition;
        this.textHeight = textHeight;
    }

    private var _preferredTailPosition:String;

    private var _textHeight:uint;

    public function set textHeight(value:uint):void {
        _textHeight = value;
    }

    public function set preferredTailPosition(value:String):void {
        _preferredTailPosition = value;
    }


    private var _showNext:Boolean = true;

    private var _showPrevious:Boolean = true;

    public function set showNext(value:Boolean):void {
        _showNext = value;
    }

    public function set showPrevious(value:Boolean):void {
        _showPrevious = value;
    }


    private var _text:String;


    public function set text(value:String):void {
        _text = value;
    }


    public function showNote():void {
        component = findComponent(anchor);
        if (component == null) {
            Alert.show("could not find " + anchor);
        }
        callout = new Callout();
        callout.setStyle("backgroundColor", 0x99ccff);
        callout.setStyle("backgroundAlpha", 1);
        callout.setStyle("dropShadowEnabled", true);
        if (_preferredTailPosition != null) {
            callout.setStyle("preferredTailPosition", _preferredTailPosition);
        }
        var note:Note = new Note();
        note.text = _text;
        note.textHeight = _textHeight;
        note.showNext = _showNext;
        note.showPrevious = _showPrevious;
        note.addEventListener(NoteEvent.NEXT_NOTE, noteEvent);
        note.addEventListener(NoteEvent.PREVIOUS_NOTE, noteEvent);
        note.addEventListener(NoteEvent.CLOSE_NOTE, noteEvent);
        previousBorderStyle = component.getStyle("borderStyle");
        previousBorderColor = component.getStyle("borderColor");
        previousBorderThickness = component.getStyle("borderThickness");
        component.setStyle("borderStyle", "solid");
        component.setStyle("borderColor", 0x1111EE);
        callout.content = note;
        callout.show(component);
    }

    private var component:UIComponent;

    private var previousBorderStyle:String;
    private var previousBorderThickness:uint;
    private var previousBorderColor:uint;

    override public function forwardExecute():void {
        showNote();
    }

    override public function backwardExecute():void {
        showNote();
    }

    override public function staysOnScreen():Boolean {
        return true;
    }

    override public function destroyNote():void {
        component.setStyle("borderStyle", previousBorderStyle);
        component.setStyle("borderColor", previousBorderColor);
        component.setStyle("borderThickness", previousBorderThickness);
        callout.hide();
        callout = null;        
    }

    private var callout:Callout;

    private function noteEvent(event:NoteEvent):void {
        dispatchEvent(event);
    }

    
}
}