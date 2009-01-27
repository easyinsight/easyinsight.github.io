package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSYahooMapDefinition")]
	public class YahooMapDefinition extends GraphicDefinition
	{
		public function YahooMapDefinition()
		{
			super();
		}
		
		override public function getDataFeedType():String {
			return "Yahoo Map";
		}	
		
		override public function getLabel():String {
			return "Satellite and Street";
		}
	}
}