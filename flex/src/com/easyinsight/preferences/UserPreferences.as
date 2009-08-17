package com.easyinsight.preferences {
[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.UserPreferences")]
public class UserPreferences {

    public var introOption:Boolean;
    public var myDataOption:Boolean;
    public var marketplaceOption:Boolean;
    public var groupsOption:Boolean;
    public var solutionsOption:Boolean;
    public var kpiOption:Boolean;
    public var apiOption:Boolean;
    public var accountOption:Boolean;
    public var userPreferencesID:int;

    public function UserPreferences() {
    }
}
}