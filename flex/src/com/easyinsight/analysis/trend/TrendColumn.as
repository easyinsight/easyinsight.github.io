/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/26/11
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.trend {
import com.easyinsight.filtering.FilterDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.TrendColumn")]
public class TrendColumn {

    public var filterDefinition:FilterDefinition;
    public var label:String;

    public function TrendColumn() {
    }
}
}
