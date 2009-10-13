package com.easyinsight.account {
import mx.containers.HBox;
import mx.controls.Label;
public class AccountTypeRenderer extends HBox{

    private var account:AccountAdminTO;
    private var typeLabel:Label;

    public function AccountTypeRenderer() {
        super();
        typeLabel = new Label();
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(typeLabel);
    }

    override public function set data(val:Object):void {
        this.account = val as AccountAdminTO;
        switch (account.accountType) {
            case Account.FREE:
                typeLabel.text = "Free";
                break;
            case Account.INDIVIDUAL:
                typeLabel.text = "Individual";
                break;
            case Account.GROUP:
                typeLabel.text = "Group";
                break;
            case Account.PROFESSIONAL:
                typeLabel.text = "Professional";
                break;
            case Account.ENTERPRISE:
                typeLabel.text = "Enterprise";
                break;
        }
    }

    override public function get data():Object {
        return account;
    }
}
}