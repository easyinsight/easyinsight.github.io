package com.easyinsight.account {
import flash.events.Event;

public class UpgradeEvent extends Event {

    public static const UPGRADE_EVENT:String = "upgradeEvent";
    
    public function UpgradeEvent() {
        super(UPGRADE_EVENT)
    }

    override public function clone():Event {
        return new UpgradeEvent();
    }
}
}