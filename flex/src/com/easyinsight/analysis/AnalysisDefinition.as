package com.easyinsight.analysis
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSAnalysisDefinition")]
	public class AnalysisDefinition
	{
		public var dataFeedID:int;
		public var name:String;
		public var analysisID:int;
		public var filterDefinitions:ArrayCollection;
		public var dataScrubs:ArrayCollection = new ArrayCollection();
		public var tagCloud:ArrayCollection = new ArrayCollection();
		public var genre:String;
		public var policy:int = 2;
		public var dateCreated:Date;
		public var dateUpdated:Date;
		public var viewCount:int;
		public var ratingCount:int;
		public var ratingAverage:Number = 0;
		public var addedItems:ArrayCollection;
		public var hierarchies:ArrayCollection;
		public var rootDefinition:Boolean;
		public var canSaveDirectly:Boolean;
		public var visibleAtFeedLevel:Boolean;
		public var publiclyVisible:Boolean;
		public var marketplaceVisible:Boolean;
		
		public function AnalysisDefinition()
		{
			
		}
		
		public function getDataFeedType():String {
			return null;
		}
		
		public function getLabel():String {
			return null;
		}
		
		public function getFields():ArrayCollection {
			return null;
		}
	}
}