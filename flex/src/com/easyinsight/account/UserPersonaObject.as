package com.easyinsight.account {
import com.easyinsight.framework.UserTransferObject;
[Bindable]
[RemoteClass(alias="com.easyinsight.users.UserPersonaObject")]
public class UserPersonaObject extends UserTransferObject {

    public var persona:int;

    public function UserPersonaObject() {
        super();
    }
}
}