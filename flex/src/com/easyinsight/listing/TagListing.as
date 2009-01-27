package com.easyinsight.listing
{
	
	import com.easyinsight.administration.feed.Tag;
	import com.easyinsight.genredata.TagFocusEvent;
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.containers.Tile;
	import mx.controls.Button;
	import mx.controls.LinkButton;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	[Event(name="tagFocus", type="com.easyinsight.genredata.TagFocusEvent")]
	
	public class TagListing extends Tile
	{
		private var feedService:RemoteObject;
		private var tile:Tile;
		private var button:Button;
		private var lastButton:LinkButton;
		private var lastButtonSize:int;
		private var currentTag:String;
		
		public function TagListing()
		{
			feedService = new RemoteObject();
			feedService.destination = "feeds";
			feedService.getAllFeedTags.addEventListener(ResultEvent.RESULT, gotTags);
			feedService.getAllFeedTags.addEventListener(FaultEvent.FAULT, fault);
			setStyle("backgroundColor", "#FFFFFF");
			setStyle("backgroundAlpha", .3);					
		}
		
		public function fault(event:FaultEvent):void {
			trace(event.message.toString());
		}
		
		public function refresh(tag:String):void {
			this.currentTag = tag;
			if (tag == null) {
				feedService.getAllFeedTags.send();
			} else {
				
			}
		}

		private function gotTags(event:ResultEvent):void {
			removeAllChildren();
			var tags:ArrayCollection = feedService.getAllFeedTags.lastResult as ArrayCollection;
			for (var i:int = 0; i < tags.length; i++) {
				var tag:Tag = tags.getItemAt(i) as Tag;
				var position:Number = i / tags.length;
				var size:int;
				if (position < .25) {
					size = 16;
				} else if (position < .5) {
					size = 14;
				} else if (position < .75) {
					size = 12;
				} else {	
					size = 10;
				}
				var tagText:LinkButton = new LinkButton();
				tagText.setStyle("fontSize", size);
				tagText.setStyle("textDecoration", "underline");
				tagText.setStyle("fontColor", "#FFFFFF");
				tagText.label = tag.tagName;
				tagText.addEventListener(MouseEvent.CLICK, focusOnTag);
				addChild(tagText); 
				//tag.tagName
			}
		}		
		
		private function focusOnTag(event:MouseEvent):void {
			var tag:String = event.currentTarget.label;			
			if (lastButton != null){
				lastButton.setStyle("fontSize", lastButtonSize);
			}
			lastButton = event.currentTarget as LinkButton;
			lastButtonSize = lastButton.getStyle("fontSize");
			if (currentTag == tag) {
				dispatchEvent(new TagFocusEvent(null));
			} else {			 
				event.currentTarget.setStyle("fontSize", 24);			
				dispatchEvent(new TagFocusEvent(tag));
			}			
		}
	}
}