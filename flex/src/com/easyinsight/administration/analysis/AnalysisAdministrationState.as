package com.easyinsight.administration.analysis
{
	import com.easyinsight.administration.feed.TagCloud;
	import com.easyinsight.analysis.AnalysisDefinition;
	
	import mx.collections.ArrayCollection;
	
	[Bindable]	
	public class AnalysisAdministrationState
	{
		public var title:String;
		public var policy:int;
		public var users:ArrayCollection;
		public var tagCloud:TagCloud;
		public var genre:String;
		
		public function AnalysisAdministrationState(analysisDefinition:AnalysisDefinition)
		{
			this.title = analysisDefinition.name;
			this.policy = analysisDefinition.policy;			
			this.genre = analysisDefinition.genre;			
			this.tagCloud = analysisDefinition.tagCloud;
		}

		public function updateAnalysisDefinition(analysisDefinition:AnalysisDefinition):void {
			analysisDefinition.name = this.title;
			analysisDefinition.policy = this.policy;
			analysisDefinition.tagCloud = this.tagCloud;
			analysisDefinition.genre = this.genre;
		}
	}
}