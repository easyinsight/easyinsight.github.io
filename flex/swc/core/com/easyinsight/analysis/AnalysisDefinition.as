package com.easyinsight.analysis
{
import com.easyinsight.filtering.FilterRawData;

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

		public var dataFeedID:int;
        public var urlKey:String;
		public var name:String;
		public var analysisID:int;
		public var filterDefinitions:ArrayCollection = new ArrayCollection();
		public var dataScrubs:ArrayCollection = new ArrayCollection();
		public var tagCloud:ArrayCollection = new ArrayCollection();
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
        public var outerJoin:Boolean;
		public var publiclyVisible:Boolean;
		public var marketplaceVisible:Boolean;
        public var reportStateID:int;
        public var authorName:String;
        public var description:String;
        public var solutionVisible:Boolean;
        public var temporaryReport:Boolean;
        public var fontName:String = "Tahoma";
        public var fontSize:int = 12;
        public var backgroundAlpha:Number = 1;
        public var fixedWidth:int = 0;
        public var accountVisible:Boolean;
        public var joinOverrides:ArrayCollection;
        public var embedUserName:String;
        public var embedPassword:String;
        public var insecureEmbedEnabled:Boolean;

		public function AnalysisDefinition()
		{
			reportType = type;
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

        public function createFormItems():ArrayCollection {
            var items:ArrayCollection = new ArrayCollection();
            items.addItem(new ComboBoxReportFormItem("Font Name", "fontName", fontName, this, ["Arial", "Arial Black", "Comic Sans MS",
                "Courier", "Georgia", "Impact", "Monaco", "Palatino", "Tahoma", "Times New Roman", "Trebuchet MS", "Verdana"]));
            items.addItem(new NumericReportFormItem("Font Size", "fontSize", fontSize, this, 8, 48));
            items.addItem(new NumericReportFormItem("Background Alpha", "backgroundAlpha", backgroundAlpha, this, 0, 1));
            items.addItem(new NumericReportFormItem("Fixed Report Width", "fixedWidth", fixedWidth, this, 0, 5000));
            return items;
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