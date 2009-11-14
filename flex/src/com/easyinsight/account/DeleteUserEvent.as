package com.easyinsight.account {
import flash.events.Event;
public class DeleteUserEvent extends Event {

    public static const DELETE_USER:String = "deleteUser";

    public var userID:int;

    public function DeleteUserEvent(userID:int) {
        super(DELETE_USER, true);
        this.userID = userID;
    }

    override public function clone():Event {
        return new DeleteUserEvent(userID);
    }
}
}