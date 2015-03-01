/**
 * Created by jamesboe on 2/25/15.
 */
package com.easyinsight.google {
import flash.events.Event;

public class GoogleSelectionEvent extends Event {

    public static const GOOGLE_SELECTION:String = "googleSelection";

    public var worksheetURL:String;

    public function GoogleSelectionEvent(worksheetURL:String) {
        super(GOOGLE_SELECTION, true);
        this.worksheetURL = worksheetURL;
    }

    override public function clone():Event {
        return new GoogleSelectionEvent(worksheetURL);
    }
}
}
