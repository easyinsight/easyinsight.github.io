/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/23/11
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisMeasure;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSVerticalListDefinition")]
public class VerticalListDefinition extends AnalysisDefinition {

    public var measures:ArrayCollection;
    public var column:AnalysisItem;
    public var verticalListID:int;
    public var headerWidth:int = 140;
    public var columnWidth:int = 73;
    public var patternName:String;

    public function VerticalListDefinition() {
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.verticalListID = VerticalListDefinition(savedDef).verticalListID;
    }

    override public function get type():int {
        return AnalysisDefinition.VERTICAL_LIST;
    }

    override public function populate(fields:ArrayCollection):void {
        this.measures = findItems(fields, AnalysisItemTypes.MEASURE);
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 0) {
            column = dimensions.getItemAt(0) as AnalysisItem;
        }
    }

    override public function getFields():ArrayCollection {
        var fields:Array = [];
        if (column != null) {
            fields.push(column);
        }
        for each (var measure:AnalysisMeasure in measures) {
            fields.push(measure);
        }
        return new ArrayCollection(fields);
    }
}
}
