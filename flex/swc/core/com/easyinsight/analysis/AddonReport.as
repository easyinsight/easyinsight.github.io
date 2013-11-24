/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/26/13
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AddonReport")]
public class AddonReport {

    public var reportID:int;
    public var reportName:String;

    public var selected:Boolean;

    public var useNewNaming:Boolean = false;

    public function AddonReport() {
    }
}
}
