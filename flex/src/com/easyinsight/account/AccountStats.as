package com.easyinsight.account {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountStats")]
public class AccountStats {

    public var usedSpace:Number;
    public var maxSpace:Number;
    public var coreSpace:Number;
    public var usedSpaceString:String;
    public var maxSpaceString:String;
    public var addonStorageUnits:int;

    public var coreSmallBizConnections:int;
    public var currentSmallBizConnections:int;
    public var addonSmallBizConnections:int;

    public var unlimitedQuickbaseConnections:Boolean;
    public var addonQuickbaseConnections:int;
    public var addonSalesforceConnections:int;
    public var usedQuickbaseConnections:int;
    public var usedSalesforceConnections:int;

    public var coreDesigners:int;
    public var addonDesigners:int;
    public var usedDesigners:int;
    public var reportViewers:int;

    public var availableUsers:Number;
    public var currentUsers:Number;

    public var apiUsedToday:Number;
    public var apiMaxToday:Number;
    public var statsList:ArrayCollection;
    
    public function AccountStats() {
    }
}
}