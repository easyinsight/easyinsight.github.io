package com.easyinsight.analysis.list
{
import com.easyinsight.analysis.*;
import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSListDefinition")]
	public class ListDefinition extends AnalysisDefinition
	{
		public var columns:ArrayCollection;
		public var listDefinitionID:int;
		public var showLineNumbers:Boolean;
		public var listLimitsMetadata:ListLimitsMetadata;
        public var summaryTotal:Boolean;
		
		public function ListDefinition()
		{
		}
		
		override public function getFields():ArrayCollection {
			return columns;
		}

        override public function populate(fields:ArrayCollection):void {
            columns = fields;
        }


        override public function get type():int {
            return AnalysisDefinition.LIST;
        }
    }
}