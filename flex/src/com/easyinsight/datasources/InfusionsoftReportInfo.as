/**
 * Created by jamesboe on 9/12/14.
 */
package com.easyinsight.datasources {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.infusionsoft.InfusionsoftReportInfo")]
public class InfusionsoftReportInfo {

    /*
     private List<InfusionsoftReport> infusionsoftReportList;
     private List<InfusionsoftUser> infusionsoftUserList;
     */

    public var infusionsoftReportList:ArrayCollection;
    public var infusionsoftUserList:ArrayCollection;

    public function InfusionsoftReportInfo() {
    }
}
}
