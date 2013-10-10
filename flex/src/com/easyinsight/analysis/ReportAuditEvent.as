/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/8/13
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ReportAuditEvent")]
public class ReportAuditEvent {

    public var auditType:String;
    public var eventLabel:String;

    public function ReportAuditEvent() {
    }
}
}
