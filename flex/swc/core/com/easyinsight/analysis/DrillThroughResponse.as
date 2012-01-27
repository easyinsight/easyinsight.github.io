/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/26/12
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.quicksearch.EIDescriptor;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DrillThroughResponse")]
public class DrillThroughResponse {

    public var descriptor:EIDescriptor;
    public var filters:ArrayCollection;

    public function DrillThroughResponse() {
    }
}
}
