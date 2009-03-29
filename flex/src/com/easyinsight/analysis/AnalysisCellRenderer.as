package com.easyinsight.analysis
{
	import com.easyinsight.analysis.conditions.ConditionRenderer;

import com.easyinsight.analysis.list.ListKeywordController;
import com.easyinsight.analysis.list.ListKeywordEvent;
import com.easyinsight.filtering.FilterRawData;
    import flash.events.ContextMenuEvent;
    import flash.events.MouseEvent;
	import flash.net.URLRequest;

    import flash.system.System;
    import flash.ui.ContextMenu;
    import flash.ui.ContextMenuItem;
import mx.controls.Alert;
import mx.controls.Label;
    import mx.controls.listClasses.IListItemRenderer;
    import mx.events.FlexEvent;
    import mx.formatters.Formatter;

	public class AnalysisCellRenderer extends Label implements IListItemRenderer
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

		//private var defaultLabel:Label;
		//private var linkButton:LinkButton;
		
		public function AnalysisCellRenderer() {
			super();
            addEventListener(MouseEvent.MOUSE_OVER, onMouseOver);
            addEventListener(MouseEvent.MOUSE_OUT, onMouseOut);
            addEventListener(MouseEvent.CLICK, onClick);
            var drilldownContextItem:ContextMenuItem = new ContextMenuItem("Drilldown", true);
            drilldownContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onDrilldown);
            var rollupContextItem:ContextMenuItem = new ContextMenuItem("Rollup", true);
            rollupContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onRollup);
            var copyContextItem:ContextMenuItem = new ContextMenuItem("Copy Cell", true);
            copyContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, copySelected);
            //dataSet.addEventListener(KeyboardEvent.KEY_UP, keyboardHandler);
            contextMenu = new ContextMenu();
            contextMenu.hideBuiltInItems();
            contextMenu.customItems = [ drilldownContextItem, rollupContextItem, copyContextItem ];
		}

        private function onDrilldown(event:ContextMenuEvent):void {
            drill();
        }

        private function copySelected(event:ContextMenuEvent):void {
            var field:String = analysisItem.qualifiedName();
            var formatter:Formatter = analysisItem.getFormatter();
            var objVal:Object = data[field];
            var text:String;
            if (objVal == null) {
                text = "";
            } else {
                text = formatter.format(objVal);
            }
            System.setClipboard(text);
        }

        private function onMouseOver(event:MouseEvent):void {
            if (linkable && event.ctrlKey && !linkShowing) {
                setStyle("textDecoration", "underline");
                linkShowing = true;
            }
        }

        private function onMouseOut(event:MouseEvent):void {
            if (linkShowing) {
                setStyle("textDecoration", "none");
                linkShowing = false;
            }
        }

        private function onRollup(event:ContextMenuEvent):void {
            var hierarchyItem:AnalysisHierarchyItem = _analysisItem as AnalysisHierarchyItem;
            var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel);
            if (index > 0) {
                hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index - 1) as HierarchyLevel;
                dispatchEvent(new HierarchyRollupEvent(hierarchyItem.hierarchyLevel.analysisItem));
            }
        }

        private function drill():void {
            var hierarchyItem:AnalysisHierarchyItem = _analysisItem as AnalysisHierarchyItem;
            var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel);
            if (index < (hierarchyItem.hierarchyLevels.length - 1)) {
                var dataField:String = _analysisItem.qualifiedName();
                var dataString:String = data[dataField];
                var filterRawData:FilterRawData = new FilterRawData();
                filterRawData.addPair(hierarchyItem.hierarchyLevel.analysisItem, dataString);
                hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index + 1) as HierarchyLevel;
                dispatchEvent(new HierarchyDrilldownEvent(HierarchyDrilldownEvent.DRILLDOWN, filterRawData));
            }
        }

        private function onClick(event:MouseEvent):void {
            if (linkable && event.ctrlKey) {
                if (_analysisItem is AnalysisHierarchyItem) {
                    drill();
                }
            }
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

        /*public function validateProperties():void {
			validateNow();
		}

		public function validateSize(recursive:Boolean = false):void {
			validateNow();
		}

		public function validateDisplayList():void {
			validateNow();
		}*/
			
		override public function set data(value:Object):void {
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
			
			override public function get data():Object {
				return _data;
			}
			
			private function navigate(event:MouseEvent):void {
				flash.net.navigateToURL(new URLRequest(URL));
			}
		
	}
}