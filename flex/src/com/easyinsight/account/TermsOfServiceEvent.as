package com.easyinsight.account {
import flash.events.Event;
public class TermsOfServiceEvent extends Event{

    public static const TERMS_OF_SERVICE_ACCEPTED:String = "tosAccepted";

    public function TermsOfServiceEvent() {
        super(TERMS_OF_SERVICE_ACCEPTED);
    }

    override public function clone():Event {
        return new TermsOfServiceEvent();
    }
}
}