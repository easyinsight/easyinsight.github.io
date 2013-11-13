/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/9/13
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisItemHandle")]
public class AnalysisItemHandle {

    public var analysisItemID:int;
    public var name:String;
    public var position:int = -1;
    public var selected:Boolean = false;

    public function AnalysisItemHandle() {
    }
}
}
