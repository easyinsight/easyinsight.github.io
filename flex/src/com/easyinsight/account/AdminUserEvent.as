package com.easyinsight.account {
import com.easyinsight.framework.ConsultantTO;
import com.easyinsight.framework.UserTransferObject;
import flash.events.Event;
public class AdminUserEvent extends Event{

    public static const USER_ADD:String = "userAdd";
    public static const USER_UPDATE:String = "userAdd";
    public static const CONSULTANT_ADD:String = "consultantAdd";
    public static const CONSULTANT_UPDATE:String = "consultantAdd";

    private var consultant:ConsultantTO;
    private var user:UserTransferObject;

    public function AdminUserEvent(type:String, user:UserTransferObject, consultant:ConsultantTO = null) {
        super(type);
        this.user = user;
        this.consultant = consultant;
    }


    override public function clone():Event {
        return new AdminUserEvent(type, user, consultant);
    }
}
}