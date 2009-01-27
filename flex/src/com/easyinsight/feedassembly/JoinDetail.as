package com.easyinsight.feedassembly
{
	import mx.containers.HBox;
	import mx.controls.Label;
	
	public class JoinDetail extends HBox 
	{
		private var connection:ConnectionWithInfo;
		private var sourceFeedLabel:Label;
		private var sourceKeyLabel:Label;
		private var targetKeyLabel:Label;
		private var targetFeedLabel:Label;
		
		public function JoinDetail()
		{
		}
		
		public function set feedConnection(feedConnection:ConnectionWithInfo):void {
			this.connection = feedConnection;
		}

		override protected function createChildren():void {
			super.createChildren();
			if (sourceFeedLabel == null) {
				sourceFeedLabel = new Label();
				sourceFeedLabel.setStyle("fontWeight", "bold");
				sourceFeedLabel.text = connection.sourceFeedName;
			}
			addChild(sourceFeedLabel);
			if (sourceKeyLabel == null) {
				sourceKeyLabel = new Label();
				sourceKeyLabel.text = connection.sourceJoin.createString();
			}
			addChild(sourceKeyLabel);
			if (targetKeyLabel == null) {
				targetKeyLabel = new Label();
				targetKeyLabel.text = connection.targetJoin.createString();
			}
			addChild(targetKeyLabel);
			if (targetFeedLabel == null) {
				targetFeedLabel = new Label();
				targetFeedLabel.setStyle("fontWeight", "bold");
				targetFeedLabel.text = connection.targetFeedName;
			}
			addChild(targetFeedLabel);
		}
	}
}