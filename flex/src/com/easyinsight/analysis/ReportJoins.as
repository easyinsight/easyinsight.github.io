/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/1/11
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ReportJoins")]
public class ReportJoins {

    public var joinOverrides:ArrayCollection;
    public var dataSources:ArrayCollection;

    public function ReportJoins() {
    }
}
}
