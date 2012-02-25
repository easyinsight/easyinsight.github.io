package com.easyinsight.framework
{
import com.easyinsight.analysis.ReportTypeOptions;
import com.easyinsight.guest.Scenario;
import com.easyinsight.preferences.UIConfiguration;
import com.easyinsight.preferences.UISettings;

import com.easyinsight.skin.ApplicationSkinTO;

import flash.display.Bitmap;

import flash.display.Loader;
import flash.display.LoaderInfo;

import flash.events.Event;
import flash.events.EventDispatcher;
import flash.net.SharedObject;
import flash.utils.ByteArray;

public class User extends EventDispatcher
{
    private var name:String;
    private var email:String;
    private var accountType:int;
    static private var _user:User;
    static private var notifier:UserEventNotifier;
    static private var sharedObject:SharedObject;
    public var spaceAllowed:int;
    public var nonCookieLogin:Boolean;
    private var _userName:String;
    public var accountAdmin:Boolean;
    public var userID:int;
    public var billingInformationGiven:Boolean;
    public var accountState:int;
    public var uiConfiguration:UIConfiguration;
    public var firstName:String;
    public var freeUpgradePossible:Boolean;
    public var lastLoginDate:Date;
    public var firstLogin:Boolean;
    public var accountName:String;
    public var renewalOptionPossible:Boolean;
    public var personaID:int;
    public var dateFormat:int;
    public var defaultReportSharing:Boolean;
    public var currencySymbol:String;
    public var applicationSkin:ApplicationSkinTO;
    private var _guestUser:Boolean;
    public var scenario:Scenario;
    public var apiKey:String;
    public var apiSecretKey:String;
    public var newsletterEnabled:Boolean;
    public var reportTypeOptions:ReportTypeOptions;
    public var fixedDashboardID:int;
    public var subdomainEnabled:Boolean;

    public function getDateFormat():String {
        var formatString:String;
        switch (dateFormat) {
            case 0:
                formatString = "MM/DD/YYYY HH:NN";
                break;
            case 1:
                formatString = "YYYY-MM-DD HH:NN";
                break;
            case 2:
                formatString = "DD-MM-YYYY HH:NN";
                break;
            case 3:
                formatString = "DD/MM/YYYY HH:NN";
                break;
            case 4:
                formatString = "DD.MM.YYYY HH:NN";
                break;
        }
        return formatString;
    }

    private var _reportLogo:Bitmap;

    public function get reportLogo():Bitmap {
        return _reportLogo;
    }

    public function set reportLogo(value:Bitmap):void {
        _reportLogo = value;
    }

    [Bindable(event="guestUserChanged")]
    public function get guestUser():Boolean {
        return _guestUser;
    }

    public function set guestUser(value:Boolean):void {
        if (_guestUser == value) return;
        _guestUser = value;
        dispatchEvent(new Event("guestUserChanged"));
    }

    [Bindable(event="userNameChanged")]
    public function get userName():String {
        return _userName;
    }

    public function set userName(value:String):void {
        if (_userName == value) return;
        _userName = value;
        dispatchEvent(new Event("userNameChanged"));
    }

    public function User() {

    }

    static public function initializeUser(response:UserServiceResponse):void {
        _user = new User();
        _user.name = response.name;
        _user.email = response.email;
        _user.firstName = response.firstName;
        _user.accountAdmin = response.accountAdmin;
        _user.spaceAllowed = response.spaceAllowed;
        _user.accountType = response.accountType;
        _user.userID = response.userID;        
        _user.billingInformationGiven = response.billingInformationGiven;
        _user.accountState = response.accountState;
        _user.freeUpgradePossible = response.freeUpgradePossible;
        _user.lastLoginDate = response.lastLoginDate;
        _user.firstLogin = response.firstLogin;
        _user.accountName = response.accountName;
        _user.nonCookieLogin = response.nonCookieLogin;
        _user.personaID = response.personaID;
        _user.dateFormat = response.dateFormat;
        _user.defaultReportSharing = response.defaultReportSharing;
        _user.scenario = response.scenario;
        _user.currencySymbol = response.currencySymbol;
        _user.applicationSkin = response.applicationSkin;
        _user.apiKey = response.apiKey;
        _user.apiSecretKey = response.apiSecretKey;
        _user.newsletterEnabled = response.newsletterEnabled;
        _user.guestUser = response.guestUser;
        _user.reportTypeOptions = response.reportTypeOptions;
        _user.fixedDashboardID = response.fixedDashboardID;
        _user.subdomainEnabled = response.subdomainEnabled;
        if (response.reportImage != null) {
            _user.loadBytes(response.reportImage);
        }
        
        if (response.uiSettings != null) {
            _user.uiConfiguration = UIConfiguration.fromUISettings(response.uiSettings);
        }
    }
    
    private var loader:Loader;

    private function loadBytes(bytes:ByteArray):void {
        loader = new Loader();
        loader.contentLoaderInfo.addEventListener(Event.COMPLETE, onComplete);
        loader.loadBytes(bytes);
    }

    private function onComplete(event:Event):void {
        var loaderContent:LoaderInfo = event.currentTarget as LoaderInfo;
        reportLogo = Bitmap(loaderContent.loader.content);
        loaderContent.loader.removeEventListener(Event.COMPLETE, onComplete);
        loader = null;
    }

    public function changeSettings(settings:UISettings):void {
        _user.uiConfiguration = UIConfiguration.fromUISettings(settings);
        User.getEventNotifier().dispatchEvent(new Event("uiChange"));
    }

    public function updateLabels(userName:String, fullName:String, email:String, firstName:String):void {
        this.email = email;
        this.userName = userName;
        this.name = fullName;
        this.firstName = firstName;
    }

    public function resync(userTransferObject:UserTransferObject):void {
        this.accountAdmin = userTransferObject.accountAdmin;
    }

    static public function destroy():void {
        _user = null;
    }

    static public function getEventNotifier():UserEventNotifier {
        if (notifier == null) {
            notifier = new UserEventNotifier();
        }
        return notifier;
    }

    public function getAccountType():int {
        return accountType;
    }

    public function setAccountType(type:int):void {
        this.accountType = type;
    }

    public function getEmail():String {
        return email;
    }

    public function getName():String {
        return name;
    }


    static public function getInstance():User {
        return _user;
    }

    static public function getSharedObject():SharedObject {
        return sharedObject;
    }

}
}
