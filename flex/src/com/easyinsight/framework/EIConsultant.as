package com.easyinsight.framework {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.EIConsultant")]
public class EIConsultant extends ConsultantTO{
    public var accountName:String;
    public var accountID:int;
    
    public function EIConsultant() {
        super();
    }
}
}