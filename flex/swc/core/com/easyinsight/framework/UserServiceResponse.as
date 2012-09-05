package com.easyinsight.framework
{
import com.easyinsight.analysis.ReportTypeOptions;
import com.easyinsight.guest.Scenario;
import com.easyinsight.preferences.UISettings;
import com.easyinsight.skin.ApplicationSkinTO;

import flash.utils.ByteArray;

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
    public var billingInformationGiven:Boolean;
    public var accountState:int;
    public var uiSettings:UISettings;
    public var firstName:String;
    public var freeUpgradePossible:Boolean;
    public var firstLogin:Boolean;
    public var guestUser:Boolean;
    public var lastLoginDate:Date;
    public var accountName:String;
    public var personaID:int;
    public var dateFormat:int;
    public var defaultReportSharing:Boolean;
    public var sessionCookie:String;
    public var nonCookieLogin:Boolean;
    public var scenario:Scenario;
    public var password:String;
    public var currencySymbol:String;
    public var applicationSkin:ApplicationSkinTO;
    public var apiKey:String;
    public var apiSecretKey:String;
    public var newsletterEnabled:Boolean;
    public var fixedDashboardID:int;
    public var reportTypeOptions:ReportTypeOptions;
    public var subdomainEnabled:Boolean;
    public var reportImage:ByteArray;
    public var refreshReports:Boolean;
    public var analyst:Boolean;
    public var pricingModel:int;

    public function UserServiceResponse()
        {
        super();
    }

}
}