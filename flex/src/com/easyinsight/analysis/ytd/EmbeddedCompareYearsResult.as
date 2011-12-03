/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/3/11
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.EmbeddedDataResults;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.EmbeddedCompareYearsDataResults")]
public class EmbeddedCompareYearsResult extends EmbeddedDataResults {

    public var dataSet:ArrayCollection;

    public function EmbeddedCompareYearsResult() {
    }
}
}
