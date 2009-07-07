package com.easyinsight.account {
import flash.events.Event;

public class UpgradeEvent extends Event {
    
    public function UpgradeEvent() {
        super(UPGRADE_EVENT)
    }

    public static const UPGRADE_EVENT:String = "upgradeEvent";
}
}