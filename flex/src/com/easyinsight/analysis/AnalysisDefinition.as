package com.easyinsight.analysis
{
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

		public var dataFeedID:int;
		public var name:String;
		public var analysisID:int;
		public var filterDefinitions:ArrayCollection;
		public var dataScrubs:ArrayCollection = new ArrayCollection();
		public var tagCloud:ArrayCollection = new ArrayCollection();
        public var reportType:int;
		public var genre:String;
		public var policy:int = 2;
		public var dateCreated:Date;
		public var dateUpdated:Date;
		public var viewCount:int;
		public var ratingCount:int;
		public var ratingAverage:Number = 0;
		public var addedItems:ArrayCollection;
		public var rootDefinition:Boolean;
		public var canSaveDirectly:Boolean;
		public var visibleAtFeedLevel:Boolean;
		public var publiclyVisible:Boolean;
		public var marketplaceVisible:Boolean;
        public var reportStateID:int;
        public var authorName:String;
        public var description:String;
        public var solutionVisible:Boolean;
        public var temporaryReport:Boolean;
		
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
	}
}