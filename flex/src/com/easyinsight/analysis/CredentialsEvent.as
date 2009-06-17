package com.easyinsight.analysis {
import com.easyinsight.framework.Credentials;

import flash.events.Event;

public class CredentialsEvent extends Event{

    public static const CREDENTIALS_SAVED:String = "credentialsSaved";

    public var credentials:Credentials;
    public var dataSourceID:int;

    public function CredentialsEvent(dataSourceID:int, credentials:Credentials) {
        super(CREDENTIALS_SAVED);
        this.dataSourceID = dataSourceID;
        this.credentials = credentials;
    }

    override public function clone():Event {
        return new CredentialsEvent(dataSourceID, credentials);
    }
}
}