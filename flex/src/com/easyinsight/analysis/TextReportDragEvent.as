/**
 * Created by jamesboe on 1/16/15.
 */
package com.easyinsight.analysis {
import flash.events.Event;

public class TextReportDragEvent extends CustomChangeEvent {

    public var name:String;

    public function TextReportDragEvent(name:String) {
        this.name = name;
    }


    override public function clone():Event {
        return new TextReportDragEvent(name);
    }
}
}
