package com.easyinsight.listing
{
	import mx.containers.VBox;
	import mx.controls.Image;
	import mx.controls.Spacer;

	public class ListOptionRenderer extends VBox
	{
		private var listingOption:ListingOption;
		private var image:Image;
		private var bottomSpacer:Spacer;
		private var topSpacer:Spacer;
				
		public function ListOptionRenderer()
		{
			super();
		}
		
		override public function set data(value:Object):void {
			this.listingOption = value as ListingOption;	
		}
		
		override public function get data():Object {
			return this.listingOption;			
		}
		
		override protected function createChildren():void {
			super.createChildren();
			if (topSpacer == null) {
				topSpacer = new Spacer();
				topSpacer.height = 12;
				addChild(topSpacer);
			}
			if (image == null){
				image = new Image();
				addChild(image);
			}			
			if (bottomSpacer == null){
				bottomSpacer = new Spacer();
				bottomSpacer.height = 12;
				addChild(bottomSpacer);
			}			
		}
		
		override protected function commitProperties():void {
			super.commitProperties();
			image.source = listingOption.icon;
			image.toolTip = listingOption.displayName;
		}
	}
}