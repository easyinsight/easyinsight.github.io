package com.easyinsight.listing
{
	
	import com.easyinsight.administration.feed.Tag;
	import com.easyinsight.genredata.TagFocusEvent;
	
	import flash.events.MouseEvent;

import flexlib.containers.FlowBox;

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
	
	public class TagListing extends FlowBox
	{
		private var feedService:RemoteObject;
		private var tile:Tile;
		private var button:Button;
		private var lastButton:LinkButton;
		private var currentTag:String;
        private var _solution:Boolean = false;

        public function set solution(value:Boolean):void {
            _solution = value;
        }

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
            feedService.getAllFeedTags.send(_solution);
        }
		
		public function fault(event:FaultEvent):void {
			trace(event.message.toString());
		}
		
		public function refresh(tag:String):void {
			this.currentTag = tag;
			if (tag == null) {
				feedService.getAllFeedTags.send(_solution);
			} else {
				
			}
		}

		private function gotTags(event:ResultEvent):void {
			removeAllChildren();
			var tags:ArrayCollection = feedService.getAllFeedTags.lastResult as ArrayCollection;
            for each (var tag:Tag in tags) {
                var gridItem:GridItem = new GridItem();
                var tagText:LinkButton = new LinkButton();
                tagText.setStyle("fontSize", 12);
                //tagText.setStyle("textDecoration", "underline");
                tagText.setStyle("fontColor", "#FFFFFF");
                tagText.setStyle("paddingLeft", 0);
                tagText.setStyle("paddingRight", 0);
                tagText.label = tag.tagName;
                tagText.addEventListener(MouseEvent.CLICK, focusOnTag);
                addChild(tagText);
            }            
		}		
		
		private function focusOnTag(event:MouseEvent):void {
			var tag:String = event.currentTarget.label;			
			if (lastButton != null){
				lastButton.setStyle("fontWeight", "normal");
			}
			lastButton = event.currentTarget as LinkButton;
			if (currentTag == tag) {
                currentTag = null;
				dispatchEvent(new TagFocusEvent(null));
			} else {
                currentTag = tag;
				event.currentTarget.setStyle("fontWeight", "bold");
				dispatchEvent(new TagFocusEvent(tag));
			}			
		}
	}
}