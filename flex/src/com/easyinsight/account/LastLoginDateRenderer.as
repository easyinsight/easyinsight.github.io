package com.easyinsight.account {
import mx.controls.Label;
import mx.formatters.DateFormatter;

public class LastLoginDateRenderer extends Label {

    private var account:AccountAdminTO;
    
    public function LastLoginDateRenderer() {
        super();
    }

    override public function set data(val:Object):void {
        account = val as AccountAdminTO;
        var df:DateFormatter = new DateFormatter();
        df.formatString = "MM/DD/YYYY HH:NN";
        if (account.lastUserLoginDate != null) {
            this.text = df.format(account.lastUserLoginDate);
        } else {
            this.text = "";
        }
    }

    override public function get data():Object {
        return account;
    }
}
}