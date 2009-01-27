package com.easyinsight.salesforce
{
	import com.easyinsight.feedassembly.CompositeFeedDefinition;

	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.salesforce.SalesforceFeedDefinition")]
	public class SalesforceFeedDefinition extends CompositeFeedDefinition
	{
		public function SalesforceFeedDefinition()
		{
			super();
		}
		
	}
}