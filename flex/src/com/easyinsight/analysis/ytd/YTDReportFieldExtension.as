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
[RemoteClass(alias="com.easyinsight.analysis.YTDReportFieldExtension")]
public class YTDReportFieldExtension extends ReportFieldExtension {
    
    public var benchmark:AnalysisItem;
    
    public function YTDReportFieldExtension() {
    }
}
}
