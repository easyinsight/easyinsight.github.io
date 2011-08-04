package com.easyinsight.quicksearch {
[Bindable]
[RemoteClass(alias="com.easyinsight.core.EIDescriptor")]
public class EIDescriptor {

    public static const EMPTY:int = 0;
    public static const DATA_SOURCE:int = 1;
    public static const REPORT:int = 2;
    public static const GROUP:int = 3;
    public static const GOAL_TREE:int = 4;

    public static const MY_GOALS:int = 5;

    public static const GOAL_HISTORY:int = 7;
    public static const SCORECARD:int = 9;
    public static const LOOKUP_TABLE:int = 10;
    public static const DASHBOARD:int = 11;

    public var id:int;
    public var name:String;
    public var author:String;
    public var creationDate:Date;
    public var icon:Class;
    public var urlKey:String;
    public var role:int;
    public var accountVisible:Boolean;
    
    public function EIDescriptor() {
    }

    public function getType():int {
        return 0;
    }

    public function get accountVisibleDisplay():String {
        return accountVisible ? "Yes" : "No";
    }
}
}