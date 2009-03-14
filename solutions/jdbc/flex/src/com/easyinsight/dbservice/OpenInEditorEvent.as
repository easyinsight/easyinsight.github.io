package com.easyinsight.dbservice {
import flash.events.Event;
import flash.events.Event;
public class OpenInEditorEvent extends Event{

    public static const OPEN_IN_EDITOR:String = "openInEditor";

    public var queryConfiguration:QueryConfiguration;

    public function OpenInEditorEvent(queryConfiguration:QueryConfiguration) {
        super(OPEN_IN_EDITOR, true);
        this.queryConfiguration = queryConfiguration;
    }

    override public function clone():Event {
        return new OpenInEditorEvent(queryConfiguration);
    }
}
}