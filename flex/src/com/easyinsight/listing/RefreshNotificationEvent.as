package com.easyinsight.listing {
import flash.events.Event;

public class RefreshNotificationEvent extends Event {
    public function RefreshNotificationEvent() {
        super(REFRESH_NOTIFICATION)
    }

    public static const REFRESH_NOTIFICATION:String = "refreshNotification";
}
}