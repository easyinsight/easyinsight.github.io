package com.easyinsight.analysis
{

import com.easyinsight.analysis.conditions.ConditionRenderer;
	import com.easyinsight.analysis.conditions.NoConditionRenderer;
	import com.easyinsight.analysis.formatter.FormattingConfiguration;

import mx.collections.ArrayCollection;
import mx.controls.Label;
import mx.core.UIComponent;
import mx.formatters.Formatter;
		
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisItem")]
	public class AnalysisItem
	{
		public var key:Key;
		public var analysisItemID:int;
		public var hidden:Boolean = false;
		public var formattingConfiguration:FormattingConfiguration = new FormattingConfiguration();
		public var sort:int = 0;
		public var displayName:String;
        public var width:int = 0;
        public var concrete:Boolean;        
        //public var virtualDimension:VirtualDimension;
        public var links:ArrayCollection = new ArrayCollection();
        public var highIsGood:Boolean = true;
        public var itemPosition:int = 0;
        public var filters:ArrayCollection = new ArrayCollection();
        public var lookupTableID:int = 0;
		
		public function AnalysisItem() {
			super();
		}        
		
		public function get display():String {
			if (displayName != null) {
				return displayName;
			}
			return key.createString();			
		}
		
		public function qualifiedName():String {
			return key.internalString() + getQualifiedSuffix();
		}
		
		protected function getQualifiedSuffix():String {
			return String(getType());
		}
		
		public function getType():int {
			return 0;
		}
		
		public function hasType(type:int):Boolean {
			return (getType() & type) == type;
		}
		
		public function createClientRenderer():ConditionRenderer {
			return NoConditionRenderer.getDefaultRenderer();
		}
		
		private static function iHateThis():void {
			var namedKey:NamedKey;
			var derivedKey:DerivedKey;
			var namespacedKey:NamespacedKey;
			var numericValue:NumericValue;
			var stringValue:StringValue;
			var dateValue:DateValue;
			var emptyValue:EmptyValue;
		}
		
		public function getFormatter():Formatter {
			return formattingConfiguration.getFormatter();					
		}

        public function getSortFunction():Function {
            return null;
        }
	}


}