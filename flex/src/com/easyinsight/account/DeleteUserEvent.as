package com.easyinsight.account {
import flash.events.Event;
public class DeleteUserEvent extends Event {

    public static const DELETE_USER:String = "deleteUser";

    public var userID:int;
    public var userName:String;

    public function DeleteUserEvent(userID:int, userName:String) {
        super(DELETE_USER, true);
        this.userID = userID;
        this.userName = userName;
    }

    override public function clone():Event {
        return new DeleteUserEvent(userID, userName);
    }
}
}