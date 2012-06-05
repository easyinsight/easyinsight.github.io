package com.easyinsight.account {
import mx.containers.HBox;
import mx.controls.Label;
public class AccountStatusRenderer extends HBox{

    private var statusLabel:Label;
    private var account:AccountAdminTO;

    public function AccountStatusRenderer() {
        super();
        statusLabel = new Label();
        this.setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
    }


    override protected function createChildren():void {
        super.createChildren();
        addChild(statusLabel);
    }

    override public function set data(val:Object):void {
        this.account = val as AccountAdminTO;
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
            case Account.BILLING_FAILED:
                statusLabel.text = "Billing Failed";
                break;
            case Account.CLOSING:
                statusLabel.text = "Closing";
                break;
            case Account.CLOSED:
                statusLabel.text = "Closed";
                break;
            case Account.PENDING_BILLING:
                statusLabel.text = "Pending Billing";
                break;
            case Account.SUSPENDED:
                statusLabel.text = "Suspended";
                break;
            case Account.PREPARING:
                statusLabel.text = "Preparing";
                break;
            case Account.BETA:
                statusLabel.text = "Beta";
                break;
            case Account.TRIAL:
                statusLabel.text = "Trial";
                break;
            case Account.REACTIVATION_POSSIBLE:
                statusLabel.text = "Reactivable";
                break;
            default:
                statusLabel.text = "(Unknown State)";
                break;
        }
    }

    override public function get data():Object {
        return account;
    }
}
}