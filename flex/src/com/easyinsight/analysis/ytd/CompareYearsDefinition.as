/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/3/11
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisMeasure;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSCompareYearsDefinition")]
public class CompareYearsDefinition extends AnalysisDefinition {

    public var measures:ArrayCollection;
    public var timeDimension:AnalysisItem;
    public var ytdID:int;
    public var headerWidth:int = 140;
    public var columnWidth:int = 73;
    public var patternName:String;

    public function CompareYearsDefinition() {
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.ytdID = CompareYearsDefinition(savedDef).ytdID;
    }

    override public function get type():int {
        return AnalysisDefinition.COMPARE_YEARS;
    }

    override public function populate(fields:ArrayCollection):void {
        this.measures = findItems(fields, AnalysisItemTypes.MEASURE);
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DATE);
        if (dimensions.length > 0) {
            timeDimension = dimensions.getItemAt(0) as AnalysisItem;
        }
    }

    override public function getFields():ArrayCollection {
        var fields:Array = [];
        if (timeDimension != null) {
            fields.push(timeDimension);
        }
        for each (var measure:AnalysisMeasure in measures) {
            fields.push(measure);
        }
        return new ArrayCollection(fields);
    }
}
}
