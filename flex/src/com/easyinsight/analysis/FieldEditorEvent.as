/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/14/12
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

public class FieldEditorEvent extends Event {

    public static const FIELD_EDITOR_OPENED:String = "fieldEditorOpened";
    public static const FIELD_EDITOR_CLOSED:String = "fieldEditorClosed";
    
    public var editor:AnalysisItemEditWindow;

    public function FieldEditorEvent(type:String, window:AnalysisItemEditWindow) {
        super(type,  true);
        this.editor = window;
    }

    override public function clone():Event {
        return new FieldEditorEvent(type, editor);
    }
}
}
