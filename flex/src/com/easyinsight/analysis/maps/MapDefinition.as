package com.easyinsight.analysis.maps
{
import com.easyinsight.analysis.*;
import mx.collections.ArrayCollection;
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSMapDefinition")]
	public class MapDefinition extends AnalysisDefinition
	{
		public static const USA:int = 1;
		public static const WORLD:int = 2;
		public static const EUROPE:int = 3;
		public static const ASIA:int = 4;
		public static const AMERICAS:int = 5;
		public static const MIDDLE_EAST:int = 6;
		
		public var mapType:int = WORLD;
		public var mapDefinitionID:int;
        public var measure:AnalysisItem;
        public var geography:AnalysisItem;
		
		public function MapDefinition()
		{
			super();
		}

        override public function populate(fields:ArrayCollection):void {
            var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
            if (measures.length > 0) {
                measure = measures.getItemAt(0) as AnalysisItem;
            }
            var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
            if (dimensions.length > 0) {
                geography = dimensions.getItemAt(0) as AnalysisItem;
            }
        }


        override public function get type():int {
            return AnalysisDefinition.MAP;
        }

        override public function getFields():ArrayCollection {
            return new ArrayCollection([ geography, measure]);
        }
	}
}