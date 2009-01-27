package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;
	
	import mx.collections.ArrayCollection;
	
	public class FilterRawData
	{
		[Bindable]
		public var keys:ArrayCollection = new ArrayCollection();
		public var values:ArrayCollection = new ArrayCollection();
		
		private var valuesMap:Object = new Object();
		 
		public function FilterRawData()
			{
			super();
		}
		
		public function addPair(key:AnalysisItem, value:String):void {
			keys.addItem(key);
			values.addItem(value);
			var setValues:ArrayCollection = valuesMap[key.qualifiedName()];
			if (setValues == null) {
				setValues = new ArrayCollection();
				valuesMap[key.qualifiedName()] = setValues;
			}
			setValues.addItem(value);
		}
		
		public function getKeys():ArrayCollection {
			return keys;
		}
		
		public function getValues(key:AnalysisItem):ArrayCollection {
			return valuesMap[key.qualifiedName()];
		}
		
		public function createFilterDefinitions(dimensions:ArrayCollection,
			measures:ArrayCollection, inclusive:Boolean):Array {
			var filterDefinitions:ArrayCollection = new ArrayCollection();
			var uniqueKeys:ArrayCollection = new ArrayCollection();
			var blah:Object = new Object();
			for (var i:int = 0; i < keys.length; i++) {
				var key:AnalysisItem = keys.getItemAt(i) as AnalysisItem;
				if (!uniqueKeys.contains(key)) {
					uniqueKeys.addItem(key);
					var matchedValues:ArrayCollection = new ArrayCollection(); 
					for (var j:int = 0; j < keys.length; j++) {					
						var loopKey:AnalysisItem = keys.getItemAt(j) as AnalysisItem;					
						var value:String = String(values.getItemAt(j));				
						if (loopKey == key) {						
							matchedValues.addItem(value);
						}
					}
					var filterDefinition:FilterValueDefinition = new FilterValueDefinition();
					filterDefinition.field = key;
					filterDefinition.filteredValues = matchedValues;
					filterDefinition.inclusive = inclusive;
					filterDefinitions.addItem(filterDefinition);
				}
			}
			return filterDefinitions.toArray();
		}	
	}
}