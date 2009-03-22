package com.easyinsight.analysis
{
	import com.easyinsight.commands.CommandEvent;
	
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	
	public class ListDropAreaGrouping extends HBox
	{		
		private var dropAreas:ArrayCollection = new ArrayCollection();
		private var _dropAreaType:Class;
		
		private var _unlimited:Boolean = false;
		private var _maxElements:int = -1;
		private var hardStop:Boolean = false;
		private var _analysisItems:ArrayCollection;

		private var iHateActionScript:ListDropArea;			
		
		public function ListDropAreaGrouping() {
			super();
			//addEventListener(FlexEvent.CREATION_COMPLETE, initializeState);
		}

        public function invalidateItems(items:ArrayCollection):void {
            for each (var itemID:int in items) {
                for each (var dropArea:DropArea in dropAreas) {
                    if (dropArea.analysisItem != null && dropArea.analysisItem.analysisItemID == itemID) {
                        removeChild(dropArea);
                        var index:int = dropAreas.getItemIndex(dropArea);
                        dropAreas.removeItemAt(index);
                    }
                }
            }
        }

        public function drilldown(field:String):void {
            for (var i:int = 0; i < dropAreas.length; i++) {
                var dropArea:DropArea = dropAreas.getItemAt(i) as DropArea;
                var analysisItem:AnalysisItem = dropArea.analysisItem;
                if (analysisItem != null && analysisItem.qualifiedName() == field) {
                    var analysisHierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
                    var index:int = analysisHierarchyItem.hierarchyLevels.getItemIndex(analysisHierarchyItem.hierarchyLevel);
                    if (index < (analysisHierarchyItem.hierarchyLevels.length - 1)) {
                        var nextLevel:HierarchyLevel = analysisHierarchyItem.hierarchyLevels.getItemAt(index + 1) as HierarchyLevel;
                        analysisHierarchyItem.hierarchyLevel = nextLevel;
                    }
                }
            }
        }

        public function set maxElements(val:int):void {
            _maxElements = val;
        }
        
        public function set unlimited(unlimited:Boolean):void {
			this._unlimited = unlimited;
		}
		
		public function set dropAreaType(dropAreaType:Class):void {
			this._dropAreaType = dropAreaType;
		}
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			this._analysisItems = analysisItems;
		}
		
		public function destroyLast():void {
			var lastDropArea:DropArea = dropAreas.getItemAt(dropAreas.length - 1) as DropArea;
			dropAreas.removeItemAt(dropAreas.length - 1);
			removeChild(lastDropArea);	
		}
		
		public function getChildrenState():Array {
            var childArray:Array = [];
            for each (var dropArea:DropArea in getChildren()) {
                childArray.push(new DropAreaState(dropArea, dropArea.analysisItem));
            }
			return childArray;
		}
		
		public function applyChildState(children:Array):void {
			this.removeAllChildren();
			dropAreas.removeAll();
            for each (var dropAreaState:DropAreaState in children) {
                var dropArea:DropArea = dropAreaState.dropArea;
                addChild(dropArea);
                dropArea.analysisItem = dropAreaState.analysisItem;
                dropAreas.addItem(dropArea);
            }
			//newDropAreaCheck();
		}


        override protected function createChildren():void {
            super.createChildren();
            var dropArea:DropArea = createDropArea();
            addChild(dropArea);
        }

        private function initializeState(event:Event):void {
	    	// create one GenericDropArea
	    	if (this.getChildren().length == 0) {
		    	var dropArea:DropArea = createDropArea();
		    	addChild(dropArea);
		    	//onItem(null, null);
		    }	    		    	
	    }	    
	    
	    private function createDropArea():DropArea {
	    	var dropArea:DropArea = new _dropAreaType();
	    	dropArea.analysisItems = _analysisItems;
	    	dropArea.addEventListener(DropAreaUpdateEvent.DROP_AREA_UPDATE, dropAreaUpdated);
	    	dropArea.addEventListener(DropAreaAddedEvent.DROP_AREA_ADD, dropAreaFilledIn);
	    	dropArea.addEventListener(DropAreaDeletionEvent.DROP_AREA_DELETE, deleteOccurred);	    
	    	dropAreas.addItem(dropArea);	
	    	return dropArea;	
	    }
	    
	    private function dropAreaUpdated(event:DropAreaUpdateEvent):void {
            dispatchEvent(new AnalysisChangedEvent(false));
	    	onItem(event.analysisItem, event.previousAnalysisItem);
	    	dispatchEvent(new AnalysisItemUpdateEvent());	
	    }
	    
	    protected function onItem(analysisItem:AnalysisItem, previousItem:AnalysisItem):void {
	    	if (previousItem == null) {

		    }
	    }
	    
	    private function dropAreaFilledIn(event:DropAreaAddedEvent):void {
            dispatchEvent(new AnalysisChangedEvent(false));
	    	newDropAreaCheck();
	    	onItem(event.analysisItem, null);	    	
	    	dispatchEvent(new AnalysisItemUpdateEvent());
	    }

	    public function reset(maxElements:int):void {
            this.maxElements = maxElements;
	    	removeAllChildren();
	    	this.dropAreas = new ArrayCollection();
            initializeState(null);
	    }
	    
	    public function reorder(keys:Array):void {
	    	removeAllChildren();
	    	var reorders:ArrayCollection = new ArrayCollection();	    	
	    	var remainder:ArrayCollection = new ArrayCollection();	    	
	    	for (var i:int = 0; i < dropAreas.length; i++) {	    		
	    		var dropArea:DropArea = dropAreas.getItemAt(i) as DropArea;
	    		if (dropArea.analysisItem != null) {
	    			reorders.addItem(dropArea);
	    		} else {
	    			remainder.addItem(dropArea);
	    		}
	    	}
	    	
	    	dropAreas = new ArrayCollection();
	    	for (i = 0; i < keys.length; i++) {
	    		var key:String = keys[i];
	    		for (var j:int = 0; j < reorders.length; j++) {
	    			dropArea = reorders.getItemAt(j) as DropArea;
	    			if (dropArea.analysisItem.qualifiedName() == key) {
    					dropAreas.addItem(dropArea);
    					addChild(dropArea);	    				
    				}
    			} 
    		}
	    	for (var k:int = 0; k < remainder.length; k++) {
	    		var remainingDropArea:DropArea = remainder.getItemAt(k) as DropArea;
	    		dropAreas.addItem(remainingDropArea);
	    		addChild(remainingDropArea);
	    	}
	    }
	    
	    public function getAvailableDropArea():DropArea {
	    	return null;
	    }
	    
	    public function newAnalysisItem(analysisItem:AnalysisItem):void {
	    	var dropArea:DropArea = dropAreas.getItemAt(dropAreas.length - 1) as DropArea;
	    	dispatchEvent(new CommandEvent(new DropAreaAddedCommand(dropArea, analysisItem)));
	    }
	    
	    public function addAnalysisItem(analysisItem:AnalysisItem, position:int = -1):void {
	    	var dropArea:DropArea = new _dropAreaType();
	    	dropArea.analysisItems = _analysisItems;
	    	dropArea.analysisItem = analysisItem;
	   		dropArea.addEventListener(DropAreaUpdateEvent.DROP_AREA_UPDATE, dropAreaUpdated);
	    	dropArea.addEventListener(DropAreaAddedEvent.DROP_AREA_ADD, dropAreaFilledIn);
	    	dropArea.addEventListener(DropAreaDeletionEvent.DROP_AREA_DELETE, deleteOccurred);
	    	var targetLocation:int = position == -1 ? dropAreas.length - 1 : position;
	    	dropAreas.addItemAt(dropArea, targetLocation);
	    	addChildAt(dropArea, targetLocation);
            if (getCurrentElements() == _maxElements) {
                destroyLast();
            }
	    	onItem(analysisItem, null);
	    }

	    private function newDropAreaCheck():void {
	    	var addNewDropArea:Boolean;
            if (_maxElements == -1) {
                addNewDropArea = true;
            } else {
                var currentElements:int = getCurrentElements();
                addNewDropArea = currentElements < _maxElements;
            }
            if (addNewDropArea) {
                addChild(createDropArea());
            }
	    }

	    public function getListColumns():Array {
	    	var columns:Array = [];
	    	for (var i:int = 0; i < dropAreas.length; i++) {
	    		var dropArea:DropArea = dropAreas.getItemAt(i) as DropArea;
	    		if (dropArea.getDropAreaType() != null) {
		    		var analysisItem:AnalysisItem = dropArea.createAnalysisItem();
		    		if (analysisItem != null) {
		    			columns[i] = analysisItem;
	    			}
	    		}
	    	}
	    	return columns;	
	    }
	    
	    private function deleteOccurred(event:DropAreaDeletionEvent):void {
			var dropArea:DropArea = event.dropArea;
			removeChild(dropArea);
			var index:int = dropAreas.getItemIndex(dropArea);
	    	dropAreas.removeItemAt(index);

	    	dispatchEvent(new AnalysisItemUpdateEvent());		
			if (dropAreas.length == 0) {
				initializeState(null);
			}		    
	    }
	    
	    private function getCurrentElements():int {
	    	var currentElements:int = 0;
	    	for (var i:int = 0; i < dropAreas.length; i++) {
	    		var dropArea:DropArea = dropAreas.getItemAt(i) as DropArea;
	    		if (dropArea.analysisItem != null) {
					currentElements++;	    			
	    		}
	    	}
	    	return currentElements;	
	    }
        public function get maxElements():int {
            return _maxElements;
        }
    }
}