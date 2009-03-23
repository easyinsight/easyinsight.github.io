package com.easyinsight.analysis
{
	import com.easyinsight.analysis.conditions.ConditionRenderer;
	
	import flash.events.MouseEvent;
	import flash.net.URLRequest;
	
import mx.controls.Alert;
import mx.controls.listClasses.IListItemRenderer;
	import mx.core.UITextField;
import mx.events.FlexEvent;
import mx.formatters.Formatter;

	public class AnalysisCellRenderer extends UITextField implements IListItemRenderer	
	{
		private var _data:Object;
		private var URL:String;
		private static const emptyText:String = "";
		private var _analysisItem:AnalysisItem;
		private var _renderer:ConditionRenderer;
		//private var defaultLabel:Label;
		//private var linkButton:LinkButton;
		
		public function AnalysisCellRenderer() {
			super();
			/*this.renderer = renderer;
			this.analysisItem = analysisItem;*/
			//horizontalScrollPolicy = ScrollPolicy.OFF;
		}

        public function get analysisItem():AnalysisItem {
            return _analysisItem;
        }

        public function set analysisItem(val:AnalysisItem):void {
            _analysisItem = val;
        }

        public function get renderer():ConditionRenderer {
            return _renderer;
        }

        public function set renderer(val:ConditionRenderer):void {
            _renderer = val;
        }

        public function validateProperties():void {
			validateNow();
		}

		public function validateSize(recursive:Boolean = false):void {
			validateNow();
		}

		public function validateDisplayList():void {
			validateNow();
		}
			
		public function set data(value:Object):void {
			_data = value;
			if (value != null) {
                var field:String = analysisItem.qualifiedName();
                var formatter:Formatter = analysisItem.getFormatter();
				var objVal:Object = value[field];
				
				
				if (objVal == null) {
					this.text = "";
				} else {
					this.text = formatter.format(objVal);

                    //Alert.show("retrieving " + field + " produced " + objVal + " gave us formatted text = " + this.text);
					if (renderer.hasCustomColor()) {
						var color:uint = renderer.getColor(objVal);
						this.textColor = color;
						//defaultLabel.textColor = color;
					} else {
                        this.textColor = 0x000000;
                    }
				}
			} else {
				/*if (objVal == null) {
					defaultLabel = instantiateNewLabel("");		
				}*/
				this.text = "";
			}
            invalidateProperties();
            dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
		}
			
			public function get data():Object {
				return _data;
			}
			
			private function navigate(event:MouseEvent):void {
				flash.net.navigateToURL(new URLRequest(URL));
			}
		
	}
}