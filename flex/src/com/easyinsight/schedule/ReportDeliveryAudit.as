/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/15/12
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
import com.easyinsight.framework.User;

import mx.formatters.DateFormatter;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.ReportDeliveryAudit")]
public class ReportDeliveryAudit {

    public var email:String;
    public var code:int;
    public var message:String;
    public var date:Date;

    public static var formatter:DateFormatter;

    public function ReportDeliveryAudit() {
    }

    public function get sentAt():String {
        if (formatter == null) {
            formatter = new DateFormatter();
            switch (User.getInstance().dateFormat) {
                case 0:
                    formatter.formatString = "MM/DD/YYYY HH:NN";
                    break;
                case 1:
                    formatter.formatString = "YYYY-MM-DD HH:NN";
                    break;
                case 2:
                    formatter.formatString = "DD-MM-YYYY HH:NN";
                    break;
                case 3:
                    formatter.formatString = "DD/MM/YYYY HH:NN";
                    break;
                case 4:
                    formatter.formatString = "DD.MM.YYYY HH:NN";
                    break;
            }
        }
        return formatter.format(date);
    }

    public function get success():String {
        if (code == 0) {
            return "Pending";
        } else if (code == 1) {
            return "Successful";
        } else if (code == 2) {
            return "Failed";
        }
        return "";
    }
}
}
