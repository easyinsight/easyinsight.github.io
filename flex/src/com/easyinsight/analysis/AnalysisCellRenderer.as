package com.easyinsight.analysis
{
	import com.easyinsight.analysis.conditions.ConditionRenderer;

import com.easyinsight.detail.DataDetailEvent;
import com.easyinsight.filtering.FilterRawData;
import com.easyinsight.report.ReportNavigationEvent;

import flash.events.ContextMenuEvent;
    import flash.events.MouseEvent;
	import flash.net.URLRequest;

    import flash.system.System;
    import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;

import mx.controls.Label;
    import mx.controls.listClasses.IListItemRenderer;
    import mx.events.FlexEvent;
    import mx.formatters.Formatter;
import mx.managers.CursorManager;

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

        private var lookupObj:Object = new Object();

		//private var defaultLabel:Label;
		//private var linkButton:LinkButton;
		
		public function AnalysisCellRenderer() {
			super();
            addEventListener(MouseEvent.MOUSE_OVER, onMouseOver);
            addEventListener(MouseEvent.MOUSE_OUT, onMouseOut);
            addEventListener(MouseEvent.CLICK, onClick);
		}

        override protected function commitProperties():void {
            super.commitProperties();
            var drilldownFunction:Function = null;
            var rollupFunction:Function = null;
            if (analysisItem is AnalysisHierarchyItem) {
                var hierarchy:AnalysisHierarchyItem = _analysisItem as AnalysisHierarchyItem;
                var index:int = hierarchy.hierarchyLevels.getItemIndex(hierarchy.hierarchyLevel);
                if (index < (hierarchy.hierarchyLevels.length - 1)) {
                    drilldownFunction = onDrilldown;
                }
                if (index > 0) {
                    rollupFunction = onRollup;
                }
            }
            lookupObj = new Object();
            var items:Array = [];
            if (drilldownFunction != null) {
                var drilldownContextItem:ContextMenuItem = new ContextMenuItem("Drilldown", true);
                drilldownContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, drilldownFunction);
                items.push(drilldownContextItem);
            }
            if (rollupFunction != null) {
                var rollupContextItem:ContextMenuItem = new ContextMenuItem("Rollup", true);
                rollupContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, rollupFunction);
                items.push(rollupContextItem);
            }
            var copyContextItem:ContextMenuItem = new ContextMenuItem("Copy Cell", true);
            copyContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, copySelected);
            items.push(copyContextItem);

            if (_analysisItem.links.length > 0) {
                for each (var link:Link in _analysisItem.links) {
                    if (link is URLLink) {
                        var url:URLLink = link as URLLink;
                        var urlContextItem:ContextMenuItem = new ContextMenuItem(url.label, true);
                        lookupObj[url.label] = url;
                        urlContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, urlClick);
                        items.push(urlContextItem);
                    } else if (link is DrillThrough) {
                        var drillThrough:DrillThrough = link as DrillThrough;
                        var drillContextItem:ContextMenuItem = new ContextMenuItem(drillThrough.label, true);
                        lookupObj[drillThrough.label] = drillThrough;
                        drillContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, drillthroughClick);
                        items.push(drillContextItem);
                    }
                }
            }

            PopupMenuFactory.menuFactory.assignMenu(this, items);

        }

        private function drillthroughClick(event:ContextMenuEvent):void {
            var contextItem:ContextMenuItem = event.currentTarget as ContextMenuItem;
            var drillThrough:DrillThrough = lookupObj[contextItem.caption] as DrillThrough;
            var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough.reportID);
            executor.addEventListener(ReportNavigationEvent.TO_REPORT, onReport);
            executor.send();
        }

        private function onReport(event:ReportNavigationEvent):void {
            dispatchEvent(event);
        }

        private function urlClick(event:ContextMenuEvent):void {
            var contextItem:ContextMenuItem = event.currentTarget as ContextMenuItem;
            var link:URLLink = lookupObj[contextItem.caption] as URLLink;
            var url:String = link.url;
            flash.net.navigateToURL(new URLRequest(url), "_blank");
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

        [Embed(source="../../../../assets/gear.png")]
        private var gearIcon:Class;

        private function onMouseOver(event:MouseEvent):void {
            if (!linkShowing) {
                linkShowing = true;
                CursorManager.setCursor(gearIcon);
            }
        }

        private function onMouseOut(event:MouseEvent):void {
            if (linkShowing) {
                linkShowing = false;
                CursorManager.removeAllCursors();
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

        private function details(event:ContextMenuEvent):void {
            var dataField:String = _analysisItem.qualifiedName();
            var dataString:String = data[dataField];
            var filterRawData:FilterRawData = new FilterRawData();
            filterRawData.addPair(_analysisItem, dataString);
            dispatchEvent(new DataDetailEvent(filterRawData));
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