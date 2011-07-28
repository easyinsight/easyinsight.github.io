package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisDimensionResultMetadata;
	import com.easyinsight.analysis.AnalysisItem;

import com.easyinsight.analysis.Value;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.containers.HBox;
import mx.containers.ViewStack;
import mx.controls.Alert;

import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.ComboBox;
import mx.controls.Label;
import mx.controls.ProgressBar;
import mx.events.DropdownEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

import org.efflex.mx.viewStackEffects.Slide;

public class ComboBoxFilter extends HBox implements IFilter
	{
		private var _filterDefinition:FilterValueDefinition;
		private var _feedID:int;
		private var dataService:RemoteObject;
		private var _analysisItem:AnalysisItem;
		
		private var comboBox:ComboBox;
		private var deleteButton:Button;
		private var editButton:Button;
		private var _analysisItems:ArrayCollection;

        private var _credentials:Object;

        private var _filterEnabled:Boolean;

    private var _reportID:int;

    private var _dashboardID:int;

    public function set reportID(value:int):void {
        _reportID = value;
    }

    public function set dashboardID(value:int):void {
        _dashboardID = value;
    }

        [Bindable(event="filterEnabledChanged")]
        public function get filterEnabled():Boolean {
            return _filterEnabled;
        }

        public function set filterEnabled(value:Boolean):void {
            if (_filterEnabled == value) return;
            _filterEnabled = value;
            dispatchEvent(new Event("filterEnabledChanged"));
        }

        [Bindable]
        [Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;
        
        [Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;
		
		public function ComboBoxFilter(feedID:int, analysisItem:AnalysisItem, reportID:int, dashboardID:int)
		{
			super();
			this._feedID = feedID;
			this._analysisItem = analysisItem;
            this.reportID = reportID;
            this.dashboardID = dashboardID;
            setStyle("verticalAlign", "middle");


		}

        private var _filterEditable:Boolean = true;

        public function set filterEditable(editable:Boolean):void {
            _filterEditable = editable;
        }

        public function set credentials(value:Object):void {
            _credentials = value;
        }

        public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		private function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
            window.feedID = _feedID;
            window.detailClass = ComboBoxFilterWindow;
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;
			PopUpManager.addPopUp(window, this, true);
			window.x = 50;
			window.y = 50;
		}
		
		private function onFilterEdit(event:FilterEditEvent):void {
            _analysisItem = event.filterDefinition.field;
            if (event.filterDefinition != this.filterDefinition || !FilterValueDefinition(event.filterDefinition).singleValue || FilterValueDefinition(event.filterDefinition).autoComplete) {
                dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this, event.bubbles, event.rebuild));        
            } else {
                viewStack.selectedIndex = 0;
                dataService = new RemoteObject();
			    dataService.destination = "data";
			    dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
                dataService.getAnalysisItemMetadata.send(_feedID, event.filterDefinition.field, new Date().getTimezoneOffset(), _reportID,  _dashboardID);
            }
		}

        private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            comboBox.enabled = checkbox.selected;
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }

        private var viewStack:ViewStack;
		
		override protected function createChildren():void {
			super.createChildren();
            viewStack = new ViewStack();
            viewStack.resizeToContent = true;
            //if (!_filterEditable) {
            var hbox:HBox = new HBox();
            hbox.percentHeight = 100;
            hbox.setStyle("verticalAlign", "middle");
            if (_filterDefinition == null || !_filterDefinition.toggleEnabled) {
                var checkbox:CheckBox = new CheckBox();
                checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
                checkbox.toolTip = "Click to disable this filter.";
                checkbox.addEventListener(Event.CHANGE, onChange);
                addChild(checkbox);
            }
            //}
            if (_showLabel) {
                var label:Label = new Label();
                label.text = _analysisItem.display + ":";
                addChild(label);
            } else {
                toolTip = _analysisItem.display;
            }
			if (comboBox == null) {
				comboBox = new ComboBox();
                comboBox.maxWidth = 300;
				comboBox.addEventListener(DropdownEvent.CLOSE, filterValueChanged);
				comboBox.enabled = false;				
			}
			hbox.addChild(comboBox);
            addChild(viewStack);
            if (_filterEditable) {
                if (editButton == null) {
                    editButton = new Button();
                    editButton.addEventListener(MouseEvent.CLICK, edit);
                    editButton.setStyle("icon", editIcon);
                    editButton.toolTip = "Edit";
                }
                hbox.addChild(editButton);
                if (deleteButton == null) {
                    deleteButton = new Button();
                    deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                    deleteButton.setStyle("icon", deleteIcon);
                    deleteButton.toolTip = "Delete";
                    deleteButton.enabled = false;
                }
                hbox.addChild(deleteButton);
            }


            var loadingBox:HBox = new HBox();
            loadingBox.height = 23;
            loadingBox.setStyle("verticalAlign", "middle");
            var loadingBar:ProgressBar = new ProgressBar();
            loadingBar.width = 300;
            var showEffect:Slide  = new Slide();
            showEffect.direction = "down";
            loadingBar.label = "";
            loadingBar.labelPlacement = "right";
            BindingUtils.bindProperty(loadingBar, "indeterminate", this, "valuesSet");
            loadingBar.indeterminate = true;
            loadingBox.addChild(loadingBar);
            loadingBox.setStyle("hideEffect", showEffect);
            viewStack.addChild(loadingBox);
            hbox.setStyle("showEffect", showEffect);
            viewStack.addChild(hbox);
            dataService = new RemoteObject();
			dataService.destination = "data";
			dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
			dataService.getAnalysisItemMetadata.send(_feedID, _analysisItem, new Date().getTimezoneOffset(), _reportID,  _dashboardID);
		}

    private var newFilter:Boolean = true;

    private var _loadingFromReport:Boolean = false;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

    private var _valuesSet:Boolean = true;


    [Bindable(event="valuesSetChanged")]
    public function get valuesSet():Boolean {
        return _valuesSet;
    }

    public function set valuesSet(value:Boolean):void {
        if (_valuesSet == value) return;
        _valuesSet = value;
        dispatchEvent(new Event("valuesSetChanged"));
    }

    private function filterValueChanged(event:DropdownEvent):void {
			var newValue:String = event.currentTarget.selectedLabel;

        var filterObj:Object = _filterDefinition.filteredValues.getItemAt(0);
        var selectedValue:String;
                if (filterObj is Value) {
                    selectedValue = String(filterObj.getValue());
                } else {
                    selectedValue = filterObj as String;
                }
			if (newValue != selectedValue) {
				var newFilteredValues:ArrayCollection = new ArrayCollection();
				newFilteredValues.addItem(newValue);
				_filterDefinition.filteredValues = newFilteredValues;
				dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
			} 
		}
		
		private function gotMetadata(event:ResultEvent):void {
            
			var analysisDimensionResultMetadata:AnalysisDimensionResultMetadata = dataService.getAnalysisItemMetadata.lastResult as 
				AnalysisDimensionResultMetadata;
			var strings:ArrayCollection = new ArrayCollection();
            if (analysisDimensionResultMetadata != null && analysisDimensionResultMetadata.values != null) {
                for each (var value:Value in analysisDimensionResultMetadata.values) {
                    var string:String = String(value.getValue());

                    if (!strings.contains(string)) {
                        strings.addItem(string);
                    }
                }
            }
            if (_filterDefinition != null && _filterDefinition.excludeEmpty) {
                var index:int = strings.getItemIndex("");
                if (index != -1) {
                    strings.removeItemAt(index);
                }
            }
			strings.sort = new Sort();
			strings.refresh();
            if (_filterDefinition != null && _filterDefinition.allOption) {
                strings.addItemAt("All", 0);
            }
			comboBox.dataProvider = strings;
			comboBox.rowCount = Math.min(strings.length, 15);
            if (_filterDefinition == null) {
                _filterDefinition = new FilterValueDefinition();
                _filterDefinition.field = _analysisItem;
                _filterDefinition.filteredValues = new ArrayCollection();
                _filterDefinition.inclusive = true;
                _filterDefinition.enabled = true;
                _filterDefinition.singleValue = true;
            }
			var selectedValue:String;
			if (_filterDefinition.filteredValues.length == 0 && strings.length > 0) {
				_filterDefinition.filteredValues.addItem(strings.getItemAt(0));
			}
            if (_filterDefinition.filteredValues.length > 0) {
                var filterObj:Object = _filterDefinition.filteredValues.getItemAt(0);
                if (filterObj is Value) {
                    selectedValue = String(filterObj.getValue());
                } else {
                    selectedValue = filterObj as String;
                }
                comboBox.selectedItem = selectedValue;
            }            			

			comboBox.enabled = _filterDefinition.enabled;
            if (deleteButton != null) {
			    deleteButton.enabled = true;
            }
            valuesSet = false;
            viewStack.selectedIndex = 1;
            if (!_loadingFromReport) {
                if (newFilter) {
                    dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
                    newFilter = false;
                } else {
                    dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, filterDefinition, filterDefinition, this, event.bubbles));
                }
            } else {

                loadingFromReport = false;
                newFilter = false;
            }
		}
		
		private function deleteSelf(event:MouseEvent):void {
			dispatchEvent(new FilterDeletionEvent(this));
		}
		
		public function get filterDefinition():FilterDefinition {
			return _filterDefinition;
		}
		
		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			_filterDefinition = filterDefinition as FilterValueDefinition; 	
		}

        private var _showLabel:Boolean;

        public function set showLabel(show:Boolean):void {
            _showLabel = show;
        }
    }
}