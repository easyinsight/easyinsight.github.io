package com.easyinsight.outboundnotifications {
import flash.events.Event;

public class TodoDeleteEvent extends Event{
    public function TodoDeleteEvent(todoItem:TodoEventInfo) {
        super(TODO_DELETE, true);
        this.todoItem = todoItem;
    }

    public static const TODO_DELETE:String = "todoDelete";

    public var todoItem:TodoEventInfo;}
}