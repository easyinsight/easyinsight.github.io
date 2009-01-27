package com.easyinsight.analysis
{
	import com.easyinsight.analysis.conditions.ConditionRenderer;
	
	import flash.events.MouseEvent;
	import flash.net.URLRequest;
	
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.UITextField;
	import mx.formatters.Formatter;

	public class AnalysisCellRenderer extends UITextField implements IListItemRenderer	
	{
		private var _data:Object;
		private var field:String;
		private var URL:String;
		private static const emptyText:String = "";
		private var analysisItem:AnalysisItem;
		private var formatter:Formatter;
		private var renderer:ConditionRenderer;
		//private var defaultLabel:Label;
		//private var linkButton:LinkButton;
		
		public function AnalysisCellRenderer(field:String, analysisItem:AnalysisItem, formatter:Formatter, 
			renderer:ConditionRenderer) {
			super();
			this.formatter = formatter;
			this.renderer = renderer;
			this.field = field;
			this.analysisItem = analysisItem;
			//horizontalScrollPolicy = ScrollPolicy.OFF;
		}
		
		public function validateProperties():void {
			
		}
		
		public function validateSize(recursive:Boolean = false):void {
			validateNow();	
		}
		
		public function validateDisplayList():void {
			validateNow();
		}
		
		/*override protected function measure():void {
			super.measure();
			measuredWidth = this.defaultLabel.width;
			DataGridItemRenderer
			measuredHeight = this.defaultLabel.height;
			height = measuredHeight;
			width = measuredWidth;			
		}*/
			
		public function set data(value:Object):void {
			_data = value;				
			if (value != null) {				
				var objVal:Object = value[field];
				
				
				if (objVal == null) {
					this.text = "";
					/*if (defaultLabel == null) {
						defaultLabel = instantiateNewLabel("");	
					} else {
						defaultLabel.text = "";
					}	*/				
				} else {
					this.text = formatter.format(objVal);
					
					/*if (defaultLabel == null) {
						defaultLabel = instantiateNewLabel(objVal);
					} else {
						defaultLabel.text = formatter.format(objVal);
					}*/
					if (renderer.hasCustomColor()) {
						var color:uint = renderer.getColor(objVal);
						this.textColor = color;
						//defaultLabel.textColor = color;
					}
				}
			} else {
				/*if (objVal == null) {
					defaultLabel = instantiateNewLabel("");		
				}*/
				this.text = "";
			}
			//invalidateProperties();
			//invalidateDisplayList();

				/*var label:Label= new Label();				
				if (objVal == null) {			
					label.text = emptyText;				
					addChild(label);
				} else {
					
					var stringForm:String = formatter.format(objVal);		
					
					//removeAllChildren();
					if (stringForm.indexOf("http") == 0) {
						var linkButton:LinkButton = new LinkButton();
						linkButton.label = stringForm;
						URL = stringForm;
						linkButton.addEventListener(MouseEvent.CLICK, navigate);
						addChild(linkButton); 	
					} else {
						var text:Text = new Text();
						label = new Label();
						label.text = stringForm;
						if (renderer.hasCustomColor()) {
							var color:uint = renderer.getColor(objVal);
							label.setStyle("color", color);
						}									
						label.truncateToFit = false;
						addChild(label);
					}
				}*/
		}
		
		/*private function instantiateNewLabel(value:Object):Label {
			var text:Label = new Label();
			addChild(text);
			text.text = formatter.format(value);
			//text.width = text.textWidth + 5;
			//text.height = text.textHeight + 5;		
			return text;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			this.defaultLabel.x = 0;
			this.defaultLabel.y = 0;
			//Box
		}*/
			
			public function get data():Object {
				return _data;
			}
			
			private function navigate(event:MouseEvent):void {
				flash.net.navigateToURL(new URLRequest(URL));
			}
		
	}
}