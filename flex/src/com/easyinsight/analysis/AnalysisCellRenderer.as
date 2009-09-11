package com.easyinsight.analysis
{
	import com.easyinsight.analysis.conditions.ConditionRenderer;

import com.easyinsight.detail.DataDetailEvent;
import com.easyinsight.filtering.FilterRawData;
import com.easyinsight.pseudocontext.PseudoContextWindow;
import com.easyinsight.report.ReportNavigationEvent;

import com.easyinsight.util.PopUpUtil;

import flash.events.ContextMenuEvent;
import flash.events.Event;
import flash.events.MouseEvent;
import flash.geom.Point;
import flash.net.URLRequest;

    import flash.system.System;
    import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;


import mx.controls.Label;
    import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.events.FlexEvent;
    import mx.formatters.Formatter;
import mx.managers.PopUpManager;


public class AnalysisCellRenderer extends UITextField implements IListItemRenderer
	{
		private var _data:Object;
		private var URL:String;
		private static const emptyText:String = "";
		private var _analysisItem:AnalysisItem;
		private var _renderer:ConditionRenderer;
        private var linkShowing:Boolean = false;
        private var linkable:Boolean = false;

        private var defaultBackground:uint;

        [Bindable]
        private var listContextMenu:ContextMenu;

        private var lookupObj:Object = new Object();

		//private var defaultLabel:Label;
		//private var linkButton:LinkButton;
		
		public function AnalysisCellRenderer() {
			super();
            addEventListener(MouseEvent.CLICK, onClick);
            setStyle("fontFamily", "Tahoma");
		}

        private function details(event:ContextMenuEvent):void {
            var dataField:String = _analysisItem.qualifiedName();
            var dataString:String = data[dataField];
            var filterRawData:FilterRawData = new FilterRawData();
            filterRawData.addPair(_analysisItem, dataString);
            dispatchEvent(new DataDetailEvent(filterRawData));
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
				var objVal:Object = value[field];
				
				
				if (objVal == null) {
					this.text = "";
				} else {
					this.text = formatter.format(objVal);

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