package com.easyinsight.filtering
{

	import com.easyinsight.analysis.AnalysisItem;

	import flash.display.DisplayObject;



import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.containers.Tile;


import mx.events.DragEvent;
	import mx.managers.DragManager;



[Event(name="transformAdded", type="com.easyinsight.filtering.TransformsUpdatedEvent")]
[Event(name="updatedTransforms", type="com.easyinsight.filtering.TransformsUpdatedEvent")]

	public class EmbeddedTransformContainer extends HBox
	{		
		private var filterMap:Object = new Object();
		private var filterDefinitions:ArrayCollection = new ArrayCollection();
		private var filterTile:Tile;
		private var _feedID:int;
		private var noFilters:Boolean = true;

        private var _filterEditable:Boolean = true;
        private var _showLabel:Boolean = true;


    private var _filterDefinitions:ArrayCollection;

    private var showingFeedback:Boolean = false;
			
		public function EmbeddedTransformContainer() {
			super();
		}


    public function set showLabel(value:Boolean):void {
        _showLabel = value;
    }

    public function set filterEditable(value:Boolean):void {
        _filterEditable = value;
    }

    private var blah:Boolean = false;

    public function set existingFilters(value:ArrayCollection):void {
        if (!blah && value != null) {

            blah = true;
            _filterDefinitions = value;
            for each (var filterDefinition:FilterDefinition in _filterDefinitions) {
                addFilterDefinition(filterDefinition);
            }
        }
    }

    public function clearFilter(item:AnalysisItem):void {
            for each (var filter:IFilter in filterMap) {
                if (filter.filterDefinition.field.qualifiedName() == item.qualifiedName()) {
                    filterTile.removeChild(filter as DisplayObject);
                    filterMap[filter.filterDefinition.field.qualifiedName()] = null;
                    var index:int = filterDefinitions.getItemIndex(filter.filterDefinition);
                    filterDefinitions.removeItemAt(index);
                    if (filterDefinitions.length == 0) {
                        noFilters = true;
                        //removeChild(filterTile);
                        //addChild(dropHereBox);
                    }
                }
            }
            dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
        }

        public function invalidateItems(items:ArrayCollection):void {
            for each (var itemID:int in items) {
                for each (var filter:IFilter in filterMap) {
                    if (filter.filterDefinition.field.analysisItemID == itemID) {
                        filterTile.removeChild(filter as DisplayObject);
                        filterMap[filter.filterDefinition.field.qualifiedName()] = null;
                        var index:int = filterDefinitions.getItemIndex(filter.filterDefinition);
                        filterDefinitions.removeItemAt(index);
                        if (filterDefinitions.length == 0) {
                            noFilters = true;
                            //removeChild(filterTile);
                            //addChild(dropHereBox);
                        }
                    }
                }
            }
        }



		
		override protected function createChildren():void {
			super.createChildren();
			if (filterTile == null){
				filterTile = new Tile();
				filterTile.percentWidth = 100;
				filterTile.percentHeight = 100;				
			}
            addChild(filterTile);
			if (noFilters && _filterDefinitions == null) {
				// addChild(dropHereBox);
			} else {
				//addChild(filterTile);
                if (_filterDefinitions != null) {
                    for each (var filter:FilterDefinition in _filterDefinitions) {
                        addFilterDefinition(filter);
                    }
                }
			}			
		}

		public function addFilterDefinition(filterDefinition:FilterDefinition):void {								
			var filter:IEmbeddedFilter = createFilter(filterDefinition);
			initializeFilter(filter);						
		}

    private var _preparedFilters:ArrayCollection;


    public function set preparedFilters(value:ArrayCollection):void {
        _preparedFilters = value;
    }

    override protected function commitProperties():void {
        super.commitProperties();
        if (_preparedFilters != null) {
            for each (var filter:FilterDefinition in _preparedFilters) {
                addFilterDefinition(filter);
            }
        }
    }

    private function createFilter(filterDefinition:FilterDefinition):IEmbeddedFilter {
			var filter:IEmbeddedFilter;
			if (filterDefinition.getType() == FilterDefinition.VALUE) {
				var filterValueDefinition:FilterValueDefinition = filterDefinition as FilterValueDefinition;
				if (filterValueDefinition.singleValue) {
					filter = new EmbeddedComboBoxFilter(_feedID, filterDefinition.field);
				} else {
					filter = new EmbeddedMultiValueFilter(_feedID, filterDefinition.field);
				}
			} else if (filterDefinition.getType() == FilterDefinition.DATE) {
				filter = new EmbeddedSliderDateFilter(_feedID, filterDefinition.field);
			} else if (filterDefinition.getType() == FilterDefinition.RANGE) {
				filter = new EmbeddedSliderMeasureFilter(_feedID, filterDefinition.field);
			} else if (filterDefinition.getType() == FilterDefinition.ROLLING_DATE) {
				filter = new EmbeddedRollingRangeFilter(_feedID, filterDefinition.field);
			} else if (filterDefinition.getType() == FilterDefinition.LAST_VALUE) {
                filter = new EmbeddedLastValueFilter(_feedID, filterDefinition.field);
            } else if (filterDefinition.getType() == FilterDefinition.PATTERN) {
                filter = new EmbeddedPatternFilter(_feedID, filterDefinition.field);
            }

            filter.showLabel = _showLabel;
			filter.filterDefinition = filterDefinition;
			return filter;	
		}
		
		public function set feedID(feedID:int):void {
			this._feedID = feedID;
		}
		
		protected function dragOverHandler(event:DragEvent):void {
            if (!showingFeedback) {
			    DragManager.showFeedback(DragManager.MOVE);
                showingFeedback = true;
            }
		}

        /*private function onFilterSelection(event:FilterCreationEvent):void {
            initializeFilter(event.filter);
        }*/
		
		private function filterAdded(event:EmbeddedFilterUpdatedEvent):void {
			addFilter(event.filter);
			dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
		}
		
		private function filterUpdated(event:EmbeddedFilterUpdatedEvent):void {
			if (!filterDefinitions.contains(event.filterDefinition) || event.rebuild) {
				var index:int = filterDefinitions.getItemIndex(event.previousFilterDefinition);
				filterDefinitions.removeItemAt(index);
				var existingFilter:IEmbeddedFilter = null;
				for each (var filter:IEmbeddedFilter in filterTile.getChildren()) {
					if (filter.filterDefinition == event.previousFilterDefinition) {
						existingFilter = filter;
					}
				}
				if (existingFilter != null) {
					filterTile.removeChild(existingFilter as DisplayObject);
				}
				var newFilter:IEmbeddedFilter = createFilter(event.filterDefinition);
				initializeFilter(newFilter);
			} else { 
				dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
			}
		}
		
		private function initializeFilter(filter:IEmbeddedFilter):void {
            if (noFilters) {
				noFilters = false;
                /*if (dropHereBox.parent != null) {
				    removeChild(dropHereBox);
                    addChild(filterTile);
                }*/
				//filterTile.explicitWidth = dropHereBox.width;
			}

            filter.showLabel = _showLabel;
			filter.addEventListener(EmbeddedFilterUpdatedEvent.FILTER_ADDED, filterAdded);
			filter.addEventListener(EmbeddedFilterUpdatedEvent.FILTER_UPDATED, filterUpdated);
			filter.addEventListener(EmbeddedFilterDeletionEvent.DELETED_FILTER, filterDeleted);
			filterTile.addChild(filter as DisplayObject);
			filterTile.invalidateSize();
			this.invalidateSize();
		}		
		
		private function addFilter(filter:IEmbeddedFilter):void {
			filterMap[filter.filterDefinition.field.qualifiedName()] = filter;
			filterDefinitions.addItem(filter.filterDefinition);
								
		}
		
		public function getFilterDefinitions():ArrayCollection {
			return filterDefinitions;
		}

        public function commandFilterDelete(filter:IEmbeddedFilter):void {
            filterTile.removeChild(filter as DisplayObject);
			filterMap[filter.filterDefinition.field.qualifiedName()] = null;
			var index:int = filterDefinitions.getItemIndex(filter.filterDefinition);
			filterDefinitions.removeItemAt(index);
			if (filterDefinitions.length == 0) {
				noFilters = true;
				//removeChild(filterTile);
				//addChild(dropHereBox);
			}
			dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
        }

    public function removeAllFilters():void {
        for each (var filter:IEmbeddedFilter in filterMap) {
            commandFilterDelete(filter);
        }
    }
		
		private function filterDeleted(event:EmbeddedFilterDeletionEvent):void {
            commandFilterDelete(event.getFilter())
		}

        public function createSimpleFilter(filterRawData:FilterRawData):FilterDefinition {
            var key:AnalysisItem = filterRawData.getKeys().getItemAt(0) as AnalysisItem;
            var filterValueDefinition:FilterValueDefinition = new FilterValueDefinition();
            filterValueDefinition.field = key;
            filterValueDefinition.filteredValues = filterRawData.getValues(key);
            filterValueDefinition.inclusive = true;
            return filterValueDefinition;
        }

    public function processRawFilterData(filterRawData:FilterRawData, includeFilter:Boolean):void {
			for (var i:int = 0; i < filterRawData.getKeys().length; i++) {
				var key:AnalysisItem = filterRawData.getKeys().getItemAt(i) as AnalysisItem;
				var values:ArrayCollection = filterRawData.getValues(key);
                var uniqueValues:ArrayCollection = new ArrayCollection();
                for each (var val:Object in values) {
                    if (uniqueValues.getItemIndex(val) == -1) {
                        uniqueValues.addItem(val);
                    }
                }

                var filterValueDefinition:FilterValueDefinition = new FilterValueDefinition();
						filterValueDefinition.field = key;
						filterValueDefinition.filteredValues = uniqueValues;
						filterValueDefinition.inclusive = includeFilter;
						var filterDefinition:FilterDefinition = filterValueDefinition;

							var filter:IEmbeddedFilter = new EmbeddedComboBoxFilter(_feedID, key);
                filterMap[key.qualifiedName()] = filter;
					filter.filterDefinition = filterDefinition;
					initializeFilter(filter);



			}
		}
	}
}