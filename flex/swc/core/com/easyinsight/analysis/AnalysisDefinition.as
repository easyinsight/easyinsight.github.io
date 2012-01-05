package com.easyinsight.analysis
{
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.filtering.FilterRawData;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.skin.ImageDescriptor;

import flash.events.Event;

import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSAnalysisDefinition")]
	public class AnalysisDefinition
	{
        public static const LIST:int = 1;
        public static const CROSSTAB:int = 2;
        public static const MAP:int = 3;
        public static const COLUMN:int = 4;
        public static const COLUMN3D:int = 5;
        public static const BAR:int = 6;
        public static const BAR3D:int = 7;
        public static const PIE:int = 8;
        public static const PIE3D:int = 9;
        public static const LINE:int = 10;
        public static const LINE3D:int = 11;
        public static const AREA:int = 12;
        public static const AREA3D:int = 13;
        public static const PLOT:int = 14;
        public static const BUBBLE:int = 15;
        public static const GAUGE:int = 16;
        public static const TREE:int = 17;
        public static const TREEMAP:int = 18;
        public static const MMLINE:int = 19;

        public static const MAP_WORLD:int = 20;
        public static const MAP_USA:int = 21;
        public static const MAP_ASIA:int = 22;
        public static const MAP_AMERICAS:int = 23;
        public static const MAP_EUROPE:int = 24;
        public static const MAP_MIDDLE_EAST:int = 25;
        public static const TIMELINE:int = 26;
        public static const HEATMAP:int = 27;
        public static const MAP_AFRICA:int = 28;
        public static const GANTT:int = 29;
        public static const FORM:int = 30;
        public static const STACKED_COLUMN:int = 31;
        public static const STACKED_BAR:int = 32;
        public static const VERTICAL_LIST:int = 33;
        public static const COMBINED_VERTICAL_LIST:int = 34;
        public static const TREND:int = 35;
        public static const DIAGRAM:int = 36;
        public static const TREND_GRID:int = 37;
        public static const YTD:int = 38;
        public static const COMPARE_YEARS:int = 39;

		public var dataFeedID:int;
        public var urlKey:String;
		public var name:String;
		public var analysisID:int;
		public var filterDefinitions:ArrayCollection = new ArrayCollection();
		public var dataScrubs:ArrayCollection = new ArrayCollection();
        public var logReport:Boolean;
        public var reportType:int;
		public var policy:int = 2;
		public var dateCreated:Date;
		public var dateUpdated:Date;
		public var viewCount:int;
		public var ratingCount:int;
		public var ratingAverage:Number = 0;
		public var addedItems:ArrayCollection;
		public var canSaveDirectly:Boolean;
		public var visibleAtFeedLevel:Boolean;
		public var publiclyVisible:Boolean;
		public var marketplaceVisible:Boolean;
        public var reportStateID:int;
        public var authorName:String;
        public var description:String;
        public var dataSourceFields:Boolean;
        public var solutionVisible:Boolean;
        public var temporaryReport:Boolean;
        public var fontName:String = "Tahoma";
        public var fontSize:int = 12;
        public var backgroundAlpha:Number = 1;
        public var fixedWidth:int = 0;
        public var accountVisible:Boolean;
        public var joinOverrides:ArrayCollection;
        public var recommendedExchange:Boolean;
        public var optimized:Boolean;
        public var fullJoins:Boolean;
        public var marmotScript:String;
        public var reportRunMarmotScript:String;
        public var headerImage:ImageDescriptor;
        public var folder:int = EIDescriptor.MAIN_VIEWS_FOLDER;

		public function AnalysisDefinition()
		{
			reportType = type;
		}

        public function newFilters(filterDefinitions:ArrayCollection):ArrayCollection {
            return new ArrayCollection();
        }

        public function cleanupReport(filterDefinitions:ArrayCollection):ArrayCollection {
            var toRemove:ArrayCollection = new ArrayCollection();
            if (filterDefinitions != null) {
                for each (var filter:FilterDefinition in filterDefinitions) {
                    if (filter.trendFilter) {
                        toRemove.addItem(filter);
                        //filterDefinitions.removeItemAt(filterDefinitions.getItemIndex(filter));
                    }
                }
            }
            return toRemove;
        }

        public function supportsEmbeddedFonts():Boolean {
            return false;
        }

        public function getFont():String {
            if (fontName != null) {
                var supportsEmbedded:Boolean = supportsEmbeddedFonts();
                if (supportsEmbedded) {
                    if (fontName == "Tahoma") {
                        fontName = "Lucida Grande";
                    }
                } else {
                    if (fontName == "Lucida Grande") {
                        fontName = "Tahoma";
                    }
                }
            }
            return fontName;
        }

        public function populate(fields:ArrayCollection):void {
            
        }

        protected function findItems(fields:ArrayCollection, type:int):ArrayCollection {
            var measures:ArrayCollection = new ArrayCollection();
            for each (var field:AnalysisItem in fields) {
                if (field != null) {
                    if (field.hasType(type)) {
                        measures.addItem(field);
                    }
                }
            }
            return measures;
        }
		
		public function getFields():ArrayCollection {
			return null;
		}

        public function get type():int {
            return 0; 
        }

        public function createDefaultLimits():void {
            
        }

        public function showDrilldown(analysisItem:AnalysisItem):Boolean {
            if (analysisItem is AnalysisHierarchyItem) {
                var hierarchy:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
                var index:int = hierarchy.hierarchyLevels.getItemIndex(hierarchy.hierarchyLevel);
                return (index < (hierarchy.hierarchyLevels.length - 1));
            }
            return false;
        }

        public function showRollup(analysisItem:AnalysisItem):Boolean {
            if (analysisItem is AnalysisHierarchyItem) {
                var hierarchy:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
                var index:int = hierarchy.hierarchyLevels.getItemIndex(hierarchy.hierarchyLevel);
                return index > 0;
            }
            return false;
        }

        public function drill(analysisItem:AnalysisItem, data:Object):Event {
            var hierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
            var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel);
            if (index < (hierarchyItem.hierarchyLevels.length - 1)) {
                var dataField:String = analysisItem.qualifiedName();
                var dataString:String = data[dataField];
                var filterRawData:FilterRawData = new FilterRawData();
                filterRawData.addPair(hierarchyItem.hierarchyLevel.analysisItem, dataString);
                hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index + 1) as HierarchyLevel;
                return new HierarchyDrilldownEvent(HierarchyDrilldownEvent.DRILLDOWN, filterRawData,
                        hierarchyItem, index + 1);
            }
            return null;
        }

        public function getValue(analysisItem:AnalysisItem, data:Object):Object {
            return data[analysisItem.qualifiedName()];
        }

        public function getCoreAnalysisItem(analysisItem:AnalysisItem):AnalysisItem {
            return analysisItem;
        }

        public function fromSave(savedDef:AnalysisDefinition):void {
            reportStateID = savedDef.reportStateID;
            analysisID = savedDef.analysisID;
            urlKey = savedDef.urlKey;
        }
	}
}