/**
 * Created by jamesboe on 9/12/14.
 */
package com.easyinsight.datasources {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.infusionsoft.InfusionsoftReport")]
public class InfusionsoftReport {

    public var reportName:String;
    public var reportID:String;

    public function InfusionsoftReport() {
    }
}
}
