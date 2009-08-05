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
		
		public function addPair(key:AnalysisItem, value:Object):void {
            if (keys.getItemIndex(key) == -1) {
			    keys.addItem(key);
            }
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
	}
}