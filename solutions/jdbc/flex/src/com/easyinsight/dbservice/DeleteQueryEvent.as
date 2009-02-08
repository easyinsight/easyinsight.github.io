package com.easyinsight.dbservice {
import flash.events.Event;
import flash.events.Event;
public class DeleteQueryEvent extends Event{

    public static const DELETE_QUERY:String = "deleteQuery";

    public var queryConfiguration:QueryConfiguration;

    public function DeleteQueryEvent(queryConfiguration:QueryConfiguration) {
        super(DELETE_QUERY, true);
        this.queryConfiguration = queryConfiguration;
    }


    override public function clone():Event {
        return new DeleteQueryEvent(queryConfiguration);
    }
}
}