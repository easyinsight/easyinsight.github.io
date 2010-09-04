package com.easyinsight.framework
{
import com.easyinsight.preferences.UISettings;

[Bindable]
[RemoteClass(alias="com.easyinsight.users.UserServiceResponse")]
public class UserServiceResponse
{
    public var successful:Boolean;
    public var failureMessage:String;
    public var userID:int;
    public var accountID:int;
    public var name:String;
    public var spaceAllowed:int;
    public var accountType:int;
    public var email:String;
    public var userName:String;
    public var accountAdmin:Boolean;
    public var activated:Boolean;
    public var billingInformationGiven:Boolean;
    public var accountState:int;
    public var uiSettings:UISettings;
    public var firstName:String;
    public var freeUpgradePossible:Boolean;
    public var firstLogin:Boolean;
    public var guestUser:Boolean;
    public var lastLoginDate:Date;
    public var accountName:String;
    public var renewalOptionPossible:Boolean;
    public var personaID:int;
    public var dateFormat:int;
    public var defaultReportSharing:Boolean;
    public var sessionCookie:String;
    public var nonCookieLogin:Boolean;

    public function UserServiceResponse()
        {
        super();
    }

}
}