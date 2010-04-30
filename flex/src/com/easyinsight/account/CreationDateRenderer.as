package com.easyinsight.account {
import mx.controls.Label;
import mx.formatters.DateFormatter;

public class CreationDateRenderer extends Label {

    private var account:AccountAdminTO;
    
    public function CreationDateRenderer() {
        super();
    }

    override public function set data(val:Object):void {
        account = val as AccountAdminTO;
        var df:DateFormatter = new DateFormatter();
        df.formatString = "MM/DD/YYYY HH:NN";
        if (account.creationDate != null) {
            this.text = df.format(account.creationDate);
        } else {
            this.text = "";
        }
    }

    override public function get data():Object {
        return account;
    }
}
}