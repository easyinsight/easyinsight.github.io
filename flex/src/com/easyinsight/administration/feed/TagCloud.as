package com.easyinsight.administration.feed
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.TagCloud")]
	public class TagCloud
	{
		public var tagCloudID:Number;
		public var tags:ArrayCollection = new ArrayCollection();
		
		public function TagCloud()
		{
		}
		
		public static function fromString(string:String):ArrayCollection {
			var tags:ArrayCollection = new ArrayCollection();
			var tagArray:Array = string.split(",");
			for (var i:int = 0; i < tagArray.length; i++) {
				var tagName:String = tagArray[i];
				var tag:Tag = new Tag();
				tag.tagName = tagName;
				tags.addItem(tag);
			}
			return tags;
		}
		
		public static function toString(tags:ArrayCollection):String {
			var tagString:String = "";
			for (var i:int = 0; i < tags.length; i++) {
				var tag:Tag = tags.getItemAt(i) as Tag;
				tagString += tag.tagName + ",";
			}
			return tagString;
		}
	}
}