package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSMapDefinition")]
	public class MapDefinition extends GraphicDefinition
	{
		public static const USA:int = 1;
		public static const WORLD:int = 2;
		public static const EUROPE:int = 3;
		public static const ASIA:int = 4;
		public static const AMERICAS:int = 5;
		public static const MIDDLE_EAST:int = 6;
		
		public var mapType:int
		public var mapDefinitionID:int;
		
		public function MapDefinition()
		{
			super();
		}
	
		override public function getDataFeedType():String {
			return "Map";
		}	
		
		override public function getLabel():String {
			return "Country and State";
		}
	}
}