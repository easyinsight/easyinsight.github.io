package com.easyinsight.filtering
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FilterValueDefinition")]
	public class FilterValueDefinition extends FilterDefinition
	{
		public var filteredValues:ArrayCollection = new ArrayCollection();
		public var inclusive:Boolean;
		
		public function FilterValueDefinition()
		{
			super();
		}
		
		override public function getType():int {
			return FilterDefinition.VALUE;
		}
    }
}