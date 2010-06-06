package com.easyinsight.tour {
import com.easyinsight.DataAnalysisContainer;

import flash.events.Event;
import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

public class TutorialFramework extends EventDispatcher {

    private var notes:ArrayCollection;

    public function TutorialFramework() {
        
    }

    private function eventNote(event:NoteEvent):void {
        dispatchEvent(event.event);
    }

    private var noteAnchor:TutorialElement;

    protected function createNotes():ArrayCollection {
        return null;
    }

    public function getNotes():ArrayCollection {
        if (notes == null) {
            notes = createNotes();
        }
        return notes;
    }

    public function startTutorial(startPosition:int = 0):void {
        if (notes == null) {
            notes = createNotes();
        }
        for each (var note:TutorialElement in notes) {
            note.addEventListener(NoteEvent.PREVIOUS_NOTE, previousNote);
            note.addEventListener(NoteEvent.NEXT_NOTE, nextNote);
            note.addEventListener(NoteEvent.CLOSE_NOTE, closeNote);
            note.addEventListener(NoteEvent.EVENT_NOTE, eventNote);
        }
        var element:TutorialElement = getNextElement();
        execute(element);        
    }

    private function execute(element:TutorialElement):void {
        if (element != null) {
            element.forwardExecute();
            if (!element.staysOnScreen()) {
                var nextElement:TutorialElement = getNextElement();
                if (nextElement != null) {
                    execute(nextElement);
                }
            }
        }
    }

    private function previousExecute(element:TutorialElement):void {
        element.backwardExecute();
        if (!element.staysOnScreen()) {
            var nextElement:TutorialElement = getPreviousElement();
            if (nextElement != null) {
                previousExecute(nextElement);
            }
        }
    }

    private function getNextElement():TutorialElement {
        var targetIndex:int = 0;
        if (noteAnchor != null) {
            targetIndex = notes.getItemIndex(noteAnchor) + 1;
            noteAnchor.destroyNote();
        }
        if (targetIndex < notes.length) {
            noteAnchor = notes.getItemAt(targetIndex) as TutorialElement;
        } else {
            noteAnchor = null;
        }
        return noteAnchor;
    }

    private function getPreviousElement():TutorialElement {
        var targetIndex:int = notes.getItemIndex(noteAnchor) - 1;
        noteAnchor.destroyNote();
        noteAnchor = notes.getItemAt(targetIndex) as TutorialElement;
        return noteAnchor;
    }

    private function closeNote(event:NoteEvent):void {
        noteAnchor.destroyNote();
    }

    private function nextNote(event:NoteEvent):void {
        var element:TutorialElement = getNextElement();
        execute(element);
    }

    private function previousNote(event:NoteEvent):void {
        var element:TutorialElement = getPreviousElement();
        previousExecute(element);
    }

    private var _reportEditor:DataAnalysisContainer;

    [Bindable(event="reportEditorChanged")]
    public function get reportEditor():DataAnalysisContainer {
        return _reportEditor;
    }

    public function set reportEditor(value:DataAnalysisContainer):void {
        if (_reportEditor == value) return;
        _reportEditor = value;
        /*for each (var note:TutorialElement in notes) {
            note.baseObject = _reportEditor;
        }*/
        dispatchEvent(new Event("reportEditorChanged"));
    }

}
}