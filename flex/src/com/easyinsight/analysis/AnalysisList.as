package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.AnalysisList")]
	public class AnalysisList extends AnalysisDimension
	{
		public var analysisListID:Number;
		public var delimiter:String = ",";
		public var expanded:Boolean = false;
		
		public function AnalysisList(expanded:Boolean = false)
		{
			super(true);
			this.expanded = expanded;
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.LIST;	
		}
	}
}