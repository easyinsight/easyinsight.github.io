package com.easyinsight.analysis.scrub
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.scrubbing.TextReplaceScrub")]
	public class TextReplaceScrub extends DataScrub
	{
		public var sourceText:String;
		public var replaceText:String;
		public var regex:Boolean;
		public var caseSensitive:Boolean;
		
		public function TextReplaceScrub() {
		}

		override public function windowClass():Class {
			return TextReplaceScrubWindow;
		}
	}
}