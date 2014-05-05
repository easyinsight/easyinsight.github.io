/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/3/14
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.FieldDataSourceOrigin")]
public class FieldDataSourceOrigin {

    public var report:int;
    public var additionalReports:ArrayCollection;

    public function FieldDataSourceOrigin() {
    }
}
}
