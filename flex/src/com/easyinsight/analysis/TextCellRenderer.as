package com.easyinsight.analysis
{




import com.easyinsight.pseudocontext.PseudoContextWindow;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.controls.Text;
import mx.events.FlexEvent;
    import mx.formatters.Formatter;
import mx.managers.PopUpManager;


public class TextCellRenderer extends Text
	{
		private var _data:Object;
		private var _analysisItem:AnalysisItem;
    private var _report:AnalysisDefinition;
    private var _selectionEnabled:Boolean;

		public function TextCellRenderer() {
			super();
            addEventListener(MouseEvent.CLICK, onClick);
		}

    public function set selectionEnabled(value:Boolean):void {
        _selectionEnabled = value;
    }

    public function set rolloverIcon(value:Class):void {

    }

    public function set report(value:AnalysisDefinition):void {
        _report = value;
    }

    private function onClick(event:MouseEvent):void {
            var window:PseudoContextWindow = new PseudoContextWindow(_analysisItem, passThrough, this, _report, data);
            PopUpManager.addPopUp(window, this);
            window.x = event.stageX + 5;
            window.y = event.stageY + 5;
        }

        private function passThrough(event:Event):void {
            dispatchEvent(event);
        }

        public function get analysisItem():AnalysisItem {
            return _analysisItem;
        }

        public function set analysisItem(val:AnalysisItem):void {
            _analysisItem = val;
        }
    
		override public function set data(value:Object):void {
			_data = value;
            var text:String;
			if (value != null) {
                var field:String = analysisItem.qualifiedName();
                var formatter:Formatter = analysisItem.getFormatter();
                if (value[field] is Value) {
                    var objVal:Value = value[field];
                    if (objVal == null) {
                        text = "";
                    } else {
                        text = formatter.format(objVal.getValue());
                    }
                } else {
                    if (value[field] != null) {
                        text = formatter.format(value[field]);
                    } else {
                        text = "";
                    }

                }
			} else {
				text = "";
			}
            if (analysisItem.hasType(AnalysisItemTypes.TEXT)) {
                var analysisText:AnalysisText = analysisItem as AnalysisText;
                if (analysisText.html) {
                    this.htmlText = text;
                } else {
                    this.text = text;
                }
            } else if (analysisItem.hasType(AnalysisItemTypes.DERIVED_GROUPING)) {
                var derivedGrouping:DerivedAnalysisDimension = analysisItem as DerivedAnalysisDimension;
                if (derivedGrouping.html) {
                    this.htmlText = text;
                } else {
                    this.text = text;
                }
            }
            new StandardContextWindow(analysisItem, passThrough, this, value);
            invalidateProperties();
            dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
		}

        override public function get data():Object {
            return _data;
        }
	}
}