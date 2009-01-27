package com.easyinsight.groups
{
	import mx.containers.Tile;

	[Event(name="groupSelected", type="com.easyinsight.groups.GroupSelectedEvent")]
	
	public class MyGroupsListing extends Tile
	{
		private var groupService:RemoteObject;
		private var tile:Tile;
		private var button:Button;
		private var lastButton:LinkButton;
		private var lastButtonSize:int;
		private var currentTag:String;
		
		public function MyGroupsListing()
		{
			super();
			groupService = new RemoteObject();
			groupService.destination = "group";
			groupService.getMemberGroups.addEventListener(ResultEvent.RESULT, gotGroups);
			groupService.getMemberGroups.addEventListener(FaultEvent.FAULT, fault);
			setStyle("backgroundColor", "#FFFFFF");
			setStyle("backgroundAlpha", .3);
		}
		
		public function fault(event:FaultEvent):void {
			trace(event.message.toString());
		}
		
		public function refresh(tag:String):void {
			this.currentTag = tag;
			if (tag == null) {
				groupService.getMemberGroups.send();
			} else {
				
			}
		}

		private function gotGroups(event:ResultEvent):void {
			removeAllChildren();
			var tags:ArrayCollection = groupService.getMemberGroups.lastResult as ArrayCollection;
			for (var i:int = 0; i < tags.length; i++) {
				var tag:Tag = tags.getItemAt(i) as Tag;
				var tagText:LinkButton = new LinkButton();				
				tagText.setStyle("textDecoration", "underline");
				tagText.label = tag.tagName;
				tagText.addEventListener(MouseEvent.CLICK, focusOnTag);
				addChild(tagText); 
				//tag.tagName
			}
		}		
		
		private function focusOnTag(event:MouseEvent):void {
			var tag:String = event.currentTarget.label;			
			
			lastButton = event.currentTarget as LinkButton;
			lastButtonSize = lastButton.getStyle("fontSize");
			dispatchEvent(new GroupSelectedEvent(
			if (currentTag == tag) {
				dispatchEvent(new TagFocusEvent(null));
			} else {			 
				event.currentTarget.setStyle("fontSize", 24);			
				dispatchEvent(new TagFocusEvent(tag));
			}			
		}
	}
}