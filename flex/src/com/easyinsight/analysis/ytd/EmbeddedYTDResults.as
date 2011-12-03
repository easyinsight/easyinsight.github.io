/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/2/11
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.EmbeddedDataResults;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.EmbeddedYTDDataResults")]
public class EmbeddedYTDResults extends EmbeddedDataResults{

    public var dataSet:ArrayCollection;

    public function EmbeddedYTDResults() {
    }
}
}
