/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/26/11
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.trend {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.KPIDefinition;
import com.easyinsight.analysis.TrendReportFieldExtension;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSTrendGridDefinition")]
public class TrendGridDefinition extends KPIDefinition {

    public var trendReportID:int;
    public var sortIndex:int = 3;
    public var sortAscending:Boolean = false;

    public function TrendGridDefinition() {
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.trendReportID = TrendGridDefinition(savedDef).trendReportID;
    }

    override public function get type():int {
        return AnalysisDefinition.TREND_GRID;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection();
    }

    override public function populate(fields:ArrayCollection):void {
        measures = new ArrayCollection();
        for each (var field:AnalysisItem in fields) {
            if (field.hasType(AnalysisItemTypes.MEASURE) && field.reportFieldExtension != null && field.reportFieldExtension is TrendReportFieldExtension) {
                measures.addItem(field);
            }
        }
    }
}
}
