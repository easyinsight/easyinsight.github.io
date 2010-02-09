package com.easyinsight.analysis.treemap {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.WSTreeMapDefinition")]
public class TreeMapDefinition extends AnalysisDefinition{

    public static const DEPTH:int = 1;
    public static const DIV_RED_GREEN:int = 2;
    public static const DIV_GREEN_RED:int = 4;
    public static const QUALITATIVE:int = 3;
    public static const SEQUENTIAL:int = 5;
    public static const DIV_BLUE_YELLOW:int = 6;
    public static const DIV_YELLOW_BLUE:int = 7;
    public static const AVG_RED_GREEN:int = 8;
    public static const AVG_GREEN_RED:int = 9;
    public static const AVG_BLUE_YELLOW:int = 10;
    public static const AVG_YELLOW_BLUE:int = 11;

    public function TreeMapDefinition() {
        super();
    }

    public var measure1:AnalysisItem;
    public var measure2:AnalysisItem;
    public var hierarchy:AnalysisItem;
    public var treeMapDefinitionID:int;
    public var colorScheme:int = QUALITATIVE;

    override public function get type():int {
        return AnalysisDefinition.TREEMAP;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ measure1, measure2, hierarchy ]);
    }

    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            measure1 = measures.getItemAt(0) as AnalysisItem;
            if (measures.length > 1) {
                measure2 = measures.getItemAt(1) as AnalysisItem;
            }
        }
        var hierarchies:ArrayCollection = findItems(fields, AnalysisItemTypes.HIERARCHY);
        if (hierarchies.length > 0) {
            hierarchy = hierarchies.getItemAt(0) as AnalysisItem;
        }
    }
}
}