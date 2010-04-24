package com.easyinsight.analysis
{
	import com.easyinsight.analysis.conditions.ConditionRenderer;



import com.easyinsight.pseudocontext.PseudoContextWindow;


import flash.events.Event;
import flash.events.MouseEvent;
import flash.net.URLRequest;

    import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.events.FlexEvent;
    import mx.formatters.Formatter;
import mx.managers.PopUpManager;


public class AnalysisCellRenderer extends UITextField implements IListItemRenderer
	{
		private var _data:Object;
		private var URL:String;
		private var _analysisItem:AnalysisItem;
		private var _renderer:ConditionRenderer;
        private var linkable:Boolean = false;


		//private var defaultLabel:Label;
		//private var linkButton:LinkButton;
		
		public function AnalysisCellRenderer() {
			super();
            addEventListener(MouseEvent.CLICK, onClick);
		}


        private function onClick(event:MouseEvent):void {
            if (event.shiftKey) {
                var window:PseudoContextWindow = new PseudoContextWindow(_analysisItem, passThrough, this);
                window.data = this.data;
                PopUpManager.addPopUp(window, this);
                window.x = event.stageX + 5;
                window.y = event.stageY + 5;
            }
        }

        private function passThrough(event:Event):void {
            dispatchEvent(event);    
        }

        public function get analysisItem():AnalysisItem {
            return _analysisItem;
        }

        public function set analysisItem(val:AnalysisItem):void {
            _analysisItem = val;
            linkable = val.hasType(AnalysisItemTypes.HIERARCHY);
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
				var objVal:Value = value[field];
				
				
				if (objVal == null) {
					this.text = "";
				} else {
                    this.text = formatter.format(objVal.getValue());                    

                    //Alert.show("retrieving " + field + " produced " + objVal + " gave us formatted text = " + this.text);
					if (renderer.hasCustomColor()) {
						var color:uint = renderer.getColor(objVal);
                        setStyle("color", color);
						//this.textColor = color;
						//defaultLabel.textColor = color;
					} else {
                        //this.textColor = 0x000000;
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