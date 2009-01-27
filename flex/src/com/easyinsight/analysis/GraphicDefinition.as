package com.easyinsight.analysis
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSGraphicDefinition")]
	public class GraphicDefinition extends AnalysisDefinition
	{
		public var measures:ArrayCollection = new ArrayCollection();
		public var dimensions:ArrayCollection = new ArrayCollection();
		public var graphicDefinitionID:int;
		
		public function GraphicDefinition()
		{
		}

	}
}