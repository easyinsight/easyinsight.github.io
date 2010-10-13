package com.easyinsight.account {
import mx.formatters.DateFormatter;

[Bindable]
[RemoteClass(alias="com.easyinsight.users.InvoiceInfo")]
public class InvoiceInfo {

    public var date:Date;
    public var invoiceText:String;
    
    public function InvoiceInfo() {
    }

    public function get invoiceDate():String {
        var df:DateFormatter = new DateFormatter();
        return df.format(date);
    }
}
}