package com.easyinsight.schedule {
import com.easyinsight.administration.sharing.UserStub;
import com.easyinsight.groups.GroupDescriptor;

import flash.events.Event;

public class RecipientEvent extends Event {

    public static const ADD_RECIPIENT:String = "addRecipient";
    public static const EDIT_RECIPIENT:String = "editRecipient";
    public static const DELETE_RECIPIENT:String = "deleteRecipient";

    public var user:UserStub;
    public var email:String;
    public var group:GroupDescriptor;

    public function RecipientEvent(type:String, user:UserStub, email:String = null, group:GroupDescriptor = null) {
        super(type, true);
        this.user = user;
        this.email = email;
        this.group = group;
    }

    override public function clone():Event {
        return new RecipientEvent(type, user, email, group);
    }
}
}