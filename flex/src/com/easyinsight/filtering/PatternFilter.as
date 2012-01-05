package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.skin.ImageConstants;

import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;
	import mx.managers.PopUpManager;
	import mx.rpc.remoting.RemoteObject;

	public class PatternFilter extends HBox implements IFilter
	{
		private var _filterDefinition:FilterPatternDefinition;
		private var _analysisItem:AnalysisItem;
		private var _feedID:int;		
		private var deleteButton:Button;
		private var editButton:Button;
		private var labelText:Label;
		private var dataService:RemoteObject;		
		private var _analysisItems:ArrayCollection;

		public function PatternFilter(feedID:int, analysisItem:AnalysisItem) {
			super();
			_analysisItem = analysisItem;
			_feedID = feedID;
		}

        private var _loadingFromReport:Boolean = false;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		public function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
            window.feedID = _feedID;
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
            window.detailClass = PatternFilterWindow;
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;			
			PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
		}
		
		private function onFilterEdit(event:FilterEditEvent):void {
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this, event.bubbles, event.rebuild));
		}

        private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }
		
		override protected function createChildren():void {
			super.createChildren();
            //if (!_filterEditable) {
                var checkbox:CheckBox = new CheckBox();
                checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
                checkbox.toolTip = "Click to disable this filter.";
                checkbox.addEventListener(Event.CHANGE, onChange);
                addChild(checkbox);
            //}
            if (_filterDefinition == null) {
                _filterDefinition = new FilterPatternDefinition();
                _filterDefinition.field = _analysisItem;
            }

            labelText = new Label();
            labelText.text = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
            addChild(labelText);

            var valueLabel:Label = new Label();
            BindingUtils.bindProperty(valueLabel, "text", filterDefinition, "pattern");
            valueLabel.text = _filterDefinition.pattern;
            addChild(valueLabel);
            if (_filterEditable) {
                if (editButton == null) {
                    editButton = new Button();
                    editButton.addEventListener(MouseEvent.CLICK, edit);
                    editButton.setStyle("icon", ImageConstants.EDIT_ICON);
                    editButton.toolTip = "Edit";
                }
                addChild(editButton);
                if (deleteButton == null) {
                    deleteButton = new Button();
                    deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                    deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
                    deleteButton.toolTip = "Delete";
                }
                addChild(deleteButton);
            }
            if (_loadingFromReport) {
                _loadingFromReport = false;
            } else {
                dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
            }
		}
		
		private function deleteSelf(event:MouseEvent):void {
			dispatchEvent(new FilterDeletionEvent(this));
		}
		
		public function get filterDefinition():FilterDefinition {
			return _filterDefinition;
		}

        private var _filterEditable:Boolean = true;

        public function set filterEditable(editable:Boolean):void {
            _filterEditable = editable;
        }
		
		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			_filterDefinition = filterDefinition as FilterPatternDefinition; 	
		}
	}
}