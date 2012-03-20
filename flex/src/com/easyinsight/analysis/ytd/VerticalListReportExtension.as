/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/2/11
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReportFieldExtension;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.VerticalListReportExtension")]
public class VerticalListReportExtension extends ReportFieldExtension {

    public var lineAbove:Boolean;
    
    public function VerticalListReportExtension() {
    }
}
}
