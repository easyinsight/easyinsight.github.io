package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSCrosstabDefinition")]
	public class CrosstabDefinition extends AnalysisDefinition
	{
		public var columns:Array;
		public var measures:Array;
		public var rows:Array;
		public var crosstabDefinitionID:Number;		
		
		public function CrosstabDefinition()
		{			
		}
		
		override public function getDataFeedType():String {
			return "Crosstab";
		}
		
		override public function getLabel():String {
			return "Crosstab";
		}
	}
}