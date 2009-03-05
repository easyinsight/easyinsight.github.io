package com.easyinsight.account {
import flash.events.Event;
public class RefreshAccountsListEvent extends Event{

    public static const REFRESH_ACCOUNTS_LIST:String = "refreshAccountsList";

    public function RefreshAccountsListEvent() {
        super(REFRESH_ACCOUNTS_LIST, true);
    }


    override public function clone():Event {
        return new RefreshAccountsListEvent();
    }
}
}