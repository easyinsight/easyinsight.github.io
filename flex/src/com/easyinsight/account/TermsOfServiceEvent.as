package com.easyinsight.account {
public class TermsOfServiceEvent extends Event{

    public static const TERMS_OF_SERVICE_ACCEPTED:String = "tosAccepted";

    public function TermsOfServiceEvent() {
        super(TERMS_OF_SERVICE_ACCEPTED);
    }


}
}