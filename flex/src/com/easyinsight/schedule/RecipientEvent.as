package com.easyinsight.schedule {
import com.easyinsight.administration.sharing.UserStub;

import flash.events.Event;

public class RecipientEvent extends Event {

    public static const ADD_RECIPIENT:String = "addRecipient";
    public static const EDIT_RECIPIENT:String = "editRecipient";
    public static const DELETE_RECIPIENT:String = "deleteRecipient";

    public var user:UserStub;

    public function RecipientEvent(type:String, user:UserStub) {
        super(type, true);
        this.user = user;
    }

    override public function clone():Event {
        return new RecipientEvent(type, user);
    }
}
}