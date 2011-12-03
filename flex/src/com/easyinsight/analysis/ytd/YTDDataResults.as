/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/2/11
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.DataResults;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.YTDDataResults")]
public class YTDDataResults extends DataResults {

    public var dataSet:ArrayCollection;

    public function YTDDataResults() {
    }
}
}
