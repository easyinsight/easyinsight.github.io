package com.easyinsight.framework
{
import com.easyinsight.account.Account;
import com.easyinsight.preferences.UIConfiguration;
import com.easyinsight.preferences.UISettings;

import flash.events.Event;
import flash.events.EventDispatcher;
import flash.net.SharedObject;

public class User extends EventDispatcher
{
    private var name:String;
    private var email:String;
    private var accountType:int;
    static private var _user:User;
    static private var notifier:UserEventNotifier;
    static private var sharedObject:SharedObject;
    public var commercialEnabled:Boolean;
    public var spaceAllowed:int;
    public var nonCookieLogin:Boolean;
    private var _userName:String;
    public var accountAdmin:Boolean;
    public var userID:int;
    public var activated:Boolean;
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
        _user.activated = response.activated;
        _user.billingInformationGiven = response.billingInformationGiven;
        _user.accountState = response.accountState;
        _user.freeUpgradePossible = response.freeUpgradePossible;
        _user.lastLoginDate = response.lastLoginDate;
        _user.firstLogin = response.firstLogin;
        _user.accountName = response.accountName;
        _user.renewalOptionPossible = response.renewalOptionPossible;
        _user.nonCookieLogin = response.nonCookieLogin;
        _user.personaID = response.personaID;
        _user.dateFormat = response.dateFormat;
        _user.defaultReportSharing = response.defaultReportSharing;
        if (_user.firstLogin) {
            User.getEventNotifier().dispatchEvent(new Event("firstLogin"));
        } else if (response.renewalOptionPossible) {
            User.getEventNotifier().dispatchEvent(new Event("renewalOption"));
        } else if (response.accountType == Account.DELINQUENT) {
            User.getEventNotifier().dispatchEvent(new Event("accountDelinquent"));
        }
        if (response.uiSettings != null) {
            _user.uiConfiguration = UIConfiguration.fromUISettings(response.uiSettings);
        }
        try {
            sharedObject = SharedObject.getLocal(response.userID.toString());
            var credentialIDs:String = sharedObject.data["credentials"];
            if (credentialIDs != null && credentialIDs != "") {
                var ids:Array = credentialIDs.split(",");
                for each (var idString:String in ids) {
                    var credentials:Credentials = getCredentials(int(idString));
                    if (credentials != null) {
                        CredentialsCache.getCache().addCredentials(int(idString), credentials);
                    }
                }
            }
        } catch (e:Error) {

        }
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

    static public function getCredentials(dataSourceID:int):Credentials {
        var idString:String = dataSourceID.toString();
        if (getSharedObject() != null && getSharedObject().data[idString] != null) {
            var c:Credentials = new Credentials();
            c.userName = User.getSharedObject().data[idString].username;
            c.password = User.getSharedObject().data[idString].password;
            c.encrypted = true;
            var credentialIDs:String = getSharedObject().data["credentials"];
            if (credentialIDs == null || credentialIDs == "") {
                credentialIDs = String(dataSourceID);
                getSharedObject().data["credentials"] = credentialIDs;
                getSharedObject().flush();
            }
            return c;
        }
        return CredentialsCache.getCache().getCredentials(dataSourceID);
    }

    static public function saveCredentials(dataSourceID:int, c:Credentials):void {
        var idString:String = dataSourceID.toString();
        if (getSharedObject() != null) {
            getSharedObject().data[idString] = new Object();
            getSharedObject().data[idString].username = c.userName;
            getSharedObject().data[idString].password = c.password;
            var credentialIDs:String = getSharedObject().data["credentials"];
            if (credentialIDs == null && credentialIDs != "") {
                credentialIDs = String(dataSourceID);
            } else {
                credentialIDs = credentialIDs + "," + String(dataSourceID);
            }
            getSharedObject().data["credentials"] = credentialIDs;
            getSharedObject().flush();
        }
    }

}
}
