package com.easyinsight.analysis.scrub
{
import com.easyinsight.analysis.AnalysisItem;

[Bindable]
	[RemoteClass(alias="com.easyinsight.scrubbing.TextReplaceScrub")]
	public class TextReplaceScrub extends DataScrub
	{
		public var sourceText:String;
		public var replaceText:String;
		public var regex:Boolean;
		public var caseSensitive:Boolean;
        public var analysisItem:AnalysisItem;
		
		public function TextReplaceScrub() {
		}

		override public function windowClass():Class {
			return TextReplaceScrubWindow;
		}
	}
}