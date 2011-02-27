package com.easyinsight.analysis.crosstab
{
import com.easyinsight.analysis.*;
import mx.collections.ArrayCollection;
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSCrosstabDefinition")]
	public class CrosstabDefinition extends AnalysisDefinition
	{
		public var columns:ArrayCollection;
		public var measures:ArrayCollection;
		public var rows:ArrayCollection;
		public var crosstabDefinitionID:Number;
        public var summaryTotal:Boolean;
		
		public function CrosstabDefinition()
		{			
		}

        override public function fromSave(savedDef:AnalysisDefinition):void {
            super.fromSave(savedDef);
            this.crosstabDefinitionID = CrosstabDefinition(savedDef).crosstabDefinitionID;
        }

        override public function get type():int {
            return AnalysisDefinition.CROSSTAB;
        }

        override public function getFields():ArrayCollection {
            var fields:ArrayCollection = new ArrayCollection();
            for each (var columnItem:AnalysisItem in columns) {
                fields.addItem(columnItem);
            }
            for each (var rowItem:AnalysisItem in rows) {
                fields.addItem(rowItem);
            }
            for each (var measureItem:AnalysisItem in measures) {
                fields.addItem(measureItem);
            }
			return fields;
		}

        override public function populate(fields:ArrayCollection):void {
            var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
            if (measures.length > 0) {
                this.measures = new ArrayCollection ([ measures.getItemAt(0) as AnalysisItem ] );
            }
            var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
            if (dimensions.length > 0) {
                this.columns = new ArrayCollection ([ dimensions.getItemAt(0) as AnalysisItem ]);
                if (dimensions.length > 1) {
                    this.rows = new ArrayCollection ([ dimensions.getItemAt(1) as AnalysisItem ]);
                }
            }
        }
	}
}