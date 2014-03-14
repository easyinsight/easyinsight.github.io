package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountSettings")]
public class AccountSettings {

    public var apiEnabled:Boolean;
    public var publicData:Boolean;
    public var marketplace:Boolean;
    public var reportSharing:Boolean;
    public var groupID:int;
    public var dateFormat:int;
    public var currencySymbol:String;
    public var firstDayOfWeek:int;
    public var maxResults:int;
    public var sendEmail:Boolean;
    public var htmlView:Boolean;
    public var locale:String;
    
    public function AccountSettings() {
    }
}
}