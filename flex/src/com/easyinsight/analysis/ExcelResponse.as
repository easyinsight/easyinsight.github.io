/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/21/12
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.utils.ByteArray;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.ExcelResponse")]
public class ExcelResponse {

    public var bytes:ByteArray;
    public var reportFault:ReportFault;

    public function ExcelResponse() {
    }
}
}
