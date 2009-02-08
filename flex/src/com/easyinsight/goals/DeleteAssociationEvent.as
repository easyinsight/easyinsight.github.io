package com.easyinsight.goals {
import flash.events.Event;
import flash.events.Event;
public class DeleteAssociationEvent extends Event{

    public static const DELETE_SOLUTION:String = "deleteSolution";
    public static const DELETE_REPORT:String = "deleteReport";
    public static const DELETE_DATA_SOURCE:String = "deleteDataSource";

    public var deleteObj:Object;

    public function DeleteAssociationEvent(type:String, deleteObj:Object) {
        super(type, true);
        this.deleteObj = deleteObj;
    }


    override public function clone():Event {
        return new DeleteAssociationEvent(type, deleteObj);
    }
}
}