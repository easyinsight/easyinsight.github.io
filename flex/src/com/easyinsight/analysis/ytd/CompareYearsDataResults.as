/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/3/11
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.DataResults;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.CompareYearsDataResults")]
public class CompareYearsDataResults extends DataResults {

    public var dataSet:ArrayCollection;

    public function CompareYearsDataResults() {
    }
}
}
