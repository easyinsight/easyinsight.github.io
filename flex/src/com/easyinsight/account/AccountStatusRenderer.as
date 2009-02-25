package com.easyinsight.account {
import mx.containers.HBox;
import mx.controls.Label;
public class AccountStatusRenderer extends HBox{

    private var statusLabel:Label;
    private var account:Account;

    public function AccountStatusRenderer() {
        super();
        statusLabel = new Label();
    }

    override public function set data(val:Object):void {
        this.account = val as Account;
        switch (account.accountState) {
            case Account.ACTIVE:
                statusLabel.text = "Active";
                break;
            case Account.INACTIVE:
                statusLabel.text = "Inactive";
                break;
            case Account.DELINQUENT:
                statusLabel.text = "Delinquent";
                break;
            case Account.CLOSED:
                statusLabel.text = "Closed";
                break;
            case Account.PENDING_BILLING:
                statusLabel.text = "Closed";
                break;
            case Account.SUSPENDED:
                statusLabel.text = "Suspended";
                break;
        }
    }

    override public function get data():Object {
        return account;
    }
}
}