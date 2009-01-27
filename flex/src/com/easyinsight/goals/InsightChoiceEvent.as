package com.easyinsight.goals
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;

	public class InsightChoiceEvent extends Event
	{
		public static const INSIGHT_CHOICE:String = "insightChoice";
		
		public var insightName:String;
		public var insightID:int;
		public var fields:ArrayCollection;
		
		public function InsightChoiceEvent(insightName:String, insightID:int, fields:ArrayCollection)
		{
			super(INSIGHT_CHOICE);
			this.insightName = insightName;
			this.insightID = insightID;
			this.fields = fields;
		}
		
		override public function clone():Event {
			return new InsightChoiceEvent(insightName, insightID, fields);
		}
	}
}