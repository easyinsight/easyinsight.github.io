package com.easyinsight.analysis
{


import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.events.Event;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.core.UITextFormat;
import mx.events.FlexEvent;
    import mx.formatters.Formatter;


public class TextCellRenderer extends UITextField implements IListItemRenderer
	{
		private var _data:Object;
		private var _analysisItem:AnalysisItem;
    private var _report:AnalysisDefinition;
    private var _selectionEnabled:Boolean;

		public function TextCellRenderer() {
			super();
            this.multiline = true;
            this.wordWrap = true;
		}

    public function set selectionEnabled(value:Boolean):void {
        _selectionEnabled = value;
    }

    public function set rolloverIcon(value:Class):void {

    }

    public function set report(value:AnalysisDefinition):void {
        _report = value;
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
    
		public function set data(value:Object):void {
			_data = value;
            var text:String;
            var color:uint = 0;
			if (value != null) {
                var field:String = analysisItem.qualifiedName();
                var formatter:Formatter = analysisItem.getFormatter();
                if (value[field] is Value) {
                    var objVal:Value = value[field];
                    if (objVal == null) {
                        text = "";
                    } else {
                        if (objVal.valueExtension != null) {
                            var ext:TextValueExtension = objVal.valueExtension as TextValueExtension;
                            color = ext.color;
                        } else {
                            color = _report is ListDefinition ? ListDefinition(_report).textColor : 0;
                        }
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
            } else {
                this.text = text;
            }
            var rext:TextReportFieldExtension = analysisItem.reportFieldExtension as TextReportFieldExtension;
            var size:int = 12;
            if (rext != null && rext.size > 0) {
                size = rext.size;
            }
            var align:String = "left";
            if (rext != null && rext.align != null) {
                align = rext.align.toLowerCase();
            }
            var tf:UITextFormat = new UITextFormat(this.systemManager, "Lucida Grande", size, color);
            tf.align = align;
            setTextFormat(tf);
            new StandardContextWindow(analysisItem, passThrough, this, value);
            invalidateProperties();
            dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
		}

        public function get data():Object {
            return _data;
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
}
}