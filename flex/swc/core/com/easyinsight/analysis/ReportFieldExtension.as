/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/26/11
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import mx.utils.ObjectUtil;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ReportFieldExtension")]
public class ReportFieldExtension {

    public static const TEXT:int = 1;
    public static const YTD:int = 2;
    public static const VERTICAL_LIST:int = 3;

    public function ReportFieldExtension() {
    }

    public function updateFromSaved(reportFieldExtension:ReportFieldExtension):void {
    }

    public function clone():ReportFieldExtension {
        var extension:ReportFieldExtension = ObjectUtil.copy(this) as ReportFieldExtension;
        subclassCopy(extension);
        return extension;
    }

    protected function subclassCopy(ext:ReportFieldExtension):void {
    }
}
}
