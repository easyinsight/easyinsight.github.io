package com.easyinsight.tokens {
import flash.events.Event;

public class RevokeTokenEvent extends Event {

    public static const REVOKE_TOKEN:String = "revokeToken";

    public var tokenSpecification:TokenSpecification;

    public function RevokeTokenEvent(tokenSpecification:TokenSpecification) {
        super(REVOKE_TOKEN, true);
        this.tokenSpecification = tokenSpecification;
    }

    override public function clone():Event {
        return new RevokeTokenEvent(tokenSpecification);
    }
}
}