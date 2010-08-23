package com.easyinsight.genredata
{
import com.easyinsight.framework.PerspectiveInfo;

import flash.events.Event;

	public class AnalyzeEvent extends Event
	{
		public static const ANALYZE:String = "analyze";

        public var perspectiveInfo:PerspectiveInfo;
				
		public function AnalyzeEvent(perspectiveInfo:PerspectiveInfo)
		{
			super(ANALYZE, true);
			this.perspectiveInfo = perspectiveInfo;
		}
		
		override public function clone():Event {
			return new AnalyzeEvent(perspectiveInfo);
		}
	}
}