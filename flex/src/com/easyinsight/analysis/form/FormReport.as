package com.easyinsight.analysis.form {
import com.easyinsight.analysis.AnalysisDefinition;

import com.easyinsight.analysis.AnalysisItem;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSForm")]

public class FormReport extends AnalysisDefinition {

    public var columns:ArrayCollection;
    public var formID:int;
    public var direction:String = "Left";
    public var labelFont:String = "Tahoma";
    public var labelFontSize:int = 12;
    public var columnCount:int = 1;

    public function FormReport() {
        super();
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.formID = FormReport(savedDef).formID;
    }

    override public function getFields():ArrayCollection {
        return columns;
    }

    override public function populate(fields:ArrayCollection):void {
        columns = new ArrayCollection();
        for each (var field:AnalysisItem in fields) {
            if (field != null) {
                columns.addItem(field);
            }
        }
    }

    override public function get type():int {
        return AnalysisDefinition.FORM;
    }
}
}