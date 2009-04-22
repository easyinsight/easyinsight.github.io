package com.easyinsight.analysis
{
import com.easyinsight.administration.feed.VirtualDimension;
import com.easyinsight.analysis.conditions.ConditionRenderer;
	import com.easyinsight.analysis.conditions.NoConditionRenderer;
	import com.easyinsight.analysis.formatter.FormattingConfiguration;
	
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
        public var virtualDimension:VirtualDimension;
		
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
			return key.createString() + getQualifiedSuffix();
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
	}
}