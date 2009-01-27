package com.easyinsight.administration.feed
{
	import com.easyinsight.groups.GroupDescriptor;
	
	import mx.containers.HBox;
	import mx.controls.Image;
	import mx.controls.Label;

	public class FeedConsumerRenderer extends HBox
	{
		private var feedConsumer:FeedConsumer;
		private var iconImage:Image;
		private var typeLabel:Label;		
		
		[Bindable]
        [Embed(source="../../../../../assets/users4.png")]
        public var groupIcon:Class;
        
        [Bindable]
        [Embed(source="../../../../../assets/user_x16.png")]
        public var userIcon:Class;
        
        [Bindable]
        private var sourceImage:Class;
        
        [Bindable]
        private var labelText:String;
		
		public function FeedConsumerRenderer()
		{
			super();
		}
		
		override protected function createChildren():void {
			if (iconImage == null) {
				iconImage = new Image();
				iconImage.source = sourceImage;
			}			
			addChild(iconImage);
			if (typeLabel == null) {
				typeLabel = new Label();
				typeLabel.text = labelText;
			}
			addChild(typeLabel);
		}
		
		override public function set data(value:Object):void {
			feedConsumer = value as FeedConsumer;
			if (feedConsumer is GroupDescriptor) {
				iconImage.source = groupIcon;
				typeLabel.text = "Group";
				labelText = "Group";
			} else {
				iconImage.source = userIcon;
				typeLabel.text = "User";
				labelText = "User";
			}
		}
		
		override public function get data():Object {
			return feedConsumer;
		}
			
	}
}