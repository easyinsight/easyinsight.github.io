package com.easyinsight.listing
{
	
	import com.easyinsight.administration.feed.Tag;
	import com.easyinsight.genredata.TagFocusEvent;
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
import mx.containers.Grid;
import mx.containers.GridItem;
import mx.containers.GridRow;
import mx.containers.Tile;
	import mx.controls.Button;
	import mx.controls.LinkButton;
import mx.events.FlexEvent;
import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	[Event(name="tagFocus", type="com.easyinsight.genredata.TagFocusEvent")]
	
	public class TagListing extends Grid
	{
		private var feedService:RemoteObject;
		private var tile:Tile;
		private var button:Button;
		private var lastButton:LinkButton;
		private var currentTag:String;
		
		public function TagListing()
		{
			feedService = new RemoteObject();
			feedService.destination = "feeds";
			feedService.getAllFeedTags.addEventListener(ResultEvent.RESULT, gotTags);
			feedService.getAllFeedTags.addEventListener(FaultEvent.FAULT, fault);
			setStyle("backgroundColor", "#FFFFFF");
			setStyle("backgroundAlpha", .3);
            addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
		}

        private function onCreation(event:FlexEvent):void {
            feedService.getAllFeedTags.send();
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
            var rows:int = tags.length / 3 + (tags.length % 3 > 0 ? 1 : 0);
            for (var row:int = 0; row < rows; row++) {
                var gridRow:GridRow = new GridRow();
                addChild(gridRow);
                for (var i:int = 0; i < 3 && (row * 3 + i) < tags.length; i++) {
                    var tag:Tag = tags.getItemAt(row * 3 + i) as Tag;
                    var gridItem:GridItem = new GridItem();
                    var tagText:LinkButton = new LinkButton();
                    tagText.setStyle("fontSize", 12);
                    //tagText.setStyle("textDecoration", "underline");
                    tagText.setStyle("fontColor", "#FFFFFF");
                    tagText.label = tag.tagName;
                    tagText.addEventListener(MouseEvent.CLICK, focusOnTag);
                    gridItem.addChild(tagText);
                    gridRow.addChild(gridItem);
                }
            }
		}		
		
		private function focusOnTag(event:MouseEvent):void {
			var tag:String = event.currentTarget.label;			
			if (lastButton != null){
				lastButton.setStyle("fontWeight", "normal");
			}
			lastButton = event.currentTarget as LinkButton;
			if (currentTag == tag) {
				dispatchEvent(new TagFocusEvent(null));
			} else {
                currentTag = tag;
				event.currentTarget.setStyle("fontWeight", "bold");
				dispatchEvent(new TagFocusEvent(tag));
			}			
		}
	}
}