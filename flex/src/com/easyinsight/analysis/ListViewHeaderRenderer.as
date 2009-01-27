package com.easyinsight.analysis
{
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.Label;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridHeaderRenderer;

	public class ListViewHeaderRenderer extends AdvancedDataGridHeaderRenderer
	{
		private var button:Button;
		private var hbox:HBox;
		private var label2:Label;
		private var customHeaderText:String;
		
		[Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;
				
		public function ListViewHeaderRenderer(headerText:String)
		{
			super();
            setStyle("fontSize", 13);
            setStyle("fontFamily", "Tahoma");
			this.customHeaderText = headerText;
		}
	
		override protected function createChildren():void {
			super.createChildren();
			/*hbox = new HBox();
			hbox.setStyle("horizontalAlign", "center");
			hbox.percentWidth = 100;

			//addChild(button);
			label2 = new Label();
			label2.setStyle("fontSize", 13);
			label2.setStyle("fontFamily", Tahoma);
			label2.text = customHeaderText;
			//addChild(label2);
			//hbox.addChild(label2);
			hbox.addChild(button);						
			addChild(hbox); */
            /*button = new Button();
            button.x = 5;
			button.setStyle("paddingBottom", 3);
			button.setStyle("icon", editIcon);
            addChild(button);*/
		}
		
		override protected function measure():void {
			super.measure();
			//this.measuredHeight = measuredHeight + 5;
			//hbox.measuredWidth = this.measuredWidth;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);			
			//this.button.setActualSize(this.button.getExplicitOrMeasuredWidth(), this.button.getExplicitOrMeasuredHeight());
			//this.label2.setActualSize(this.label2.getExplicitOrMeasuredWidth(), this.label2.getExplicitOrMeasuredHeight());
			//this.hbox.setActualSize(this.hbox.getExplicitOrMeasuredWidth(), this.hbox.getExplicitOrMeasuredHeight());
		}
	}
}