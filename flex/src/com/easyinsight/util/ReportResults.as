/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/18/13
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ReportResults")]
public class ReportResults {

    public var reports:ArrayCollection;
    public var reportTags:ArrayCollection;

    public function ReportResults() {
    }
}
}
