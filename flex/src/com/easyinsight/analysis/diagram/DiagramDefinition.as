/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/11
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.diagram {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.KPIDefinition;
import com.easyinsight.analysis.TrendReportFieldExtension;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSDiagramDefinition")]
public class DiagramDefinition extends KPIDefinition {

    public var diagramReportID:int;
    public var links:ArrayCollection = new ArrayCollection();

    public function DiagramDefinition() {
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.diagramReportID = DiagramDefinition(savedDef).diagramReportID;
    }

    override public function get type():int {
        return AnalysisDefinition.DIAGRAM;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection();
    }

    override public function populate(fields:ArrayCollection):void {
        measures = new ArrayCollection();
        for each (var field:AnalysisItem in fields) {
            if (field.hasType(AnalysisItemTypes.MEASURE) && field.reportFieldExtension != null && field.reportFieldExtension is TrendReportFieldExtension) {
                var diagramExt:DiagramReportFieldExtension = new DiagramReportFieldExtension();
                diagramExt.date = TrendReportFieldExtension(field.reportFieldExtension).date;
                diagramExt.highLow = TrendReportFieldExtension(field.reportFieldExtension).highLow;
                diagramExt.iconImage = TrendReportFieldExtension(field.reportFieldExtension).iconImage;
                field.reportFieldExtension = diagramExt;
                measures.addItem(field);
            }
        }
    }
}
}
