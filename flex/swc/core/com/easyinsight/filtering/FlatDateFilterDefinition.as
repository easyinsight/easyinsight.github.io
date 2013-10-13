/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/16/11
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItemResultMetadata;
import com.easyinsight.analysis.AnalysisItemTypes;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.FlatDateFilter")]
public class FlatDateFilterDefinition extends FilterDefinition {

    public var value:int;
    public var dateLevel:int = AnalysisItemTypes.YEAR_LEVEL;
    public var cachedValues:AnalysisItemResultMetadata;

    public function FlatDateFilterDefinition() {
    }

    override public function getType():int {
        return FilterDefinition.FLAT_DATE;
    }


    override public function loadFromSharedObject(value:Object):void {
        if (value != null) {
            this.value = value as int;
        }
    }


    override public function getSaveValue():Object {
        return value;
    }
}
}
