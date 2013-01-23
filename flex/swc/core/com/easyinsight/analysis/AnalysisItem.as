package com.easyinsight.analysis
{


	import com.easyinsight.analysis.formatter.FormattingConfiguration;

import com.easyinsight.filtering.FilterDefinition;

import mx.collections.ArrayCollection;

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
		public var sortSequence:int = 0;
		public var displayName:String;
        public var width:int = 0;
        public var concrete:Boolean;
        public var links:ArrayCollection = new ArrayCollection();
        public var highIsGood:Boolean = true;
        public var itemPosition:int = 0;
        public var filters:ArrayCollection = new ArrayCollection();
        public var lookupTableID:int = 0;
        public var originalDisplayName:String;
        public var tooltip:String;
        public var reportFieldExtension:ReportFieldExtension;
        public var marmotScript:String;
        public var sortItem:AnalysisItem;
        public var keyColumn:Boolean;
        public var labelColumn:Boolean;
        public var fromField:AnalysisItem;
        public var kpi:Boolean;
        public var fieldType:int;
		
		public function AnalysisItem() {
			super();
		}        
		
		public function get display():String {
			if (displayName != null) {
				return displayName;
			}
			return key.createString();			
		}

        public function matches(analysisItem:AnalysisItem):Boolean {
            if (analysisItemID != 0 && analysisItem.analysisItemID != 0 && analysisItemID == analysisItem.analysisItemID) {
                return true;
            }
            return qualifiedName() == analysisItem.qualifiedName() && getType() == analysisItem.getType();
        }

        public function updateFromSaved(analysisItem:AnalysisItem):void {
            this.analysisItemID = analysisItem.analysisItemID;
            if (filters != null && analysisItem.filters != null) {
                for each (var itemFilter:FilterDefinition in this.filters) {
                    for each (var savedItemFilter:FilterDefinition in analysisItem.filters) {
                        if (itemFilter.matches(savedItemFilter)) {
                            itemFilter.updateFromSaved(savedItemFilter);
                        }
                    }
                }
            }
            if (reportFieldExtension != null) {
                reportFieldExtension.updateFromSaved(analysisItem.reportFieldExtension);
            }
        }
		
		public function qualifiedName():String {
			return key.internalString() + getQualifiedSuffix();
		}

        public function get qualifiedProperty():String {
            return qualifiedName();
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

        private static function iHateThis():void {
			var namedKey:NamedKey;
			var derivedKey:DerivedKey;
			var numericValue:NumericValue;
			var stringValue:StringValue;
			var dateValue:DateValue;
			var emptyValue:EmptyValue;
            var analysisDimension:AnalysisDimension;
		}
		
		public function getFormatter():Formatter {
			return formattingConfiguration.getFormatter(2, 0);
		}

        public function getSortFunction():Function {
            return null;
        }
        
        public function defaultLink():Link {
            var defaultLink:Link = null;
            if (links != null && links.length > 0) {
                for each (var link:Link in links) {
                    if (link.defaultLink) {
                        defaultLink = link;
                        break;
                    }
                }
            }
            return defaultLink;
        }
	}


}