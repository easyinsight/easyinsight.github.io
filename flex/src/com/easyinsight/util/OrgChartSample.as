package com.easyinsight.util
{
	import mx.collections.ArrayCollection;
	
	public class OrgChartSample
	{
		public var children:ArrayCollection = new ArrayCollection();
		public var name:String;
		
		public function OrgChartSample(name:String)
		{
			this.name = name;
		}

	}
}