package com.easyinsight.feedassembly
{
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.Key;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.CompositeFeedConnection")]
	public class CompositeFeedConnection
	{
		public var sourceJoin:Key;
		public var targetJoin:Key;
		public var sourceFeedID:int;
		public var targetFeedID:int;
        public var sourceItem:AnalysisItem;
        public var targetItem:AnalysisItem;
        public var sourceFeedName:String;
		public var targetFeedName:String;
        public var sourceOuterJoin:Boolean;
        public var targetOuterJoin:Boolean;
        public var sourceJoinOnOriginal:Boolean;
        public var targetJoinOnOriginal:Boolean;

		public function CompositeFeedConnection()
			{
			super();
		}

        public function get sourceDisplay():String {
            if (sourceItem != null) {
                return sourceItem.display;
            }
            return sourceJoin.createString();
        }

        public function get targetDisplay():String {
            if (targetItem != null) {
                return targetItem.display;
            }
            return targetJoin.createString();
        }
	}
}