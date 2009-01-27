package com.easyinsight.account
{
	import mx.containers.HBox;
	import mx.controls.Image;
	import mx.controls.Label;

	public class AccountComparisonImage extends HBox
	{
		public var field:String;
		public var object:Object;
		
		public function AccountComparisonImage(field:String)
		{
			super();
			this.field = field;
			this.setStyle("horizontalAlign", "center");			
		}
		
		override public function set data(value:Object):void {
			removeAllChildren();
			this.object = value;
			if (value is AccountComparisonRow) {
				var accountComparisonRow:AccountComparisonRow = value as AccountComparisonRow;
				var label:Label = new Label();
				label.text = accountComparisonRow.getText(field);
				addChild(label);				
			} else if (value is AccountComparisonIcon) {
				var accountComparisonIcon:AccountComparisonIcon = value as AccountComparisonIcon;
				var classSource:Class = accountComparisonIcon.getImage(field);
				if (classSource != null) {				 
					var image:Image = new Image();
					image.source = classSource;
					addChild(image);
				} 
			}
		}
		
		override public function get data():Object {
			return this.object;
		} 
	}
}