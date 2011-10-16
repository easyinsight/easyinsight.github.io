/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/7/11
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.TextReportFieldExtension")]
public class TextReportFieldExtension extends ReportFieldExtension {

    public var size:int;
    public var align:String;
    public var fixedWidth:int;
    public var wordWrap:Boolean;

    public function TextReportFieldExtension() {
    }
}
}
