package com.easyinsight.filtering
{
import com.easyinsight.commands.CommandEvent;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemWrapper;
import com.easyinsight.util.UserAudit;

import flash.display.DisplayObject;

import flash.geom.Point;

import flexlib.containers.FlowBox;

import mx.collections.ArrayCollection;
import mx.containers.HBox;

import mx.controls.AdvancedDataGrid;
import mx.controls.DataGrid;
import mx.controls.List;
import mx.controls.ToolTip;
import mx.core.UIComponent;
import mx.events.DragEvent;
import mx.events.FlexEvent;
import mx.managers.DragManager;
import mx.managers.PopUpManager;
import mx.managers.ToolTipManager;

[Event(name="updatedTransforms", type="com.easyinsight.filtering.TransformsUpdatedEvent")]

public class TransformContainer extends HBox
{
    private var filterMap:Object = new Object();
    private var filterDefinitions:ArrayCollection = new ArrayCollection();
    private var filterTile:FlowBox;
    private var _feedID:int;
    private var noFilters:Boolean = true;
    // private var dropHereBox:VBox;
    private var _filterEditable:Boolean = true;
    private var _showLabel:Boolean = true;
    [Bindable]
    private var _analysisItems:ArrayCollection;

    private var _filterDefinitions:ArrayCollection;

    private var showingFeedback:Boolean = false;

    public function TransformContainer() {
        super();
        this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        this.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
        this.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
    }

    protected function adapterFlowBoxUpdateCompleteHandler(event:FlexEvent):void
    {
        // resize the FlowBox manually, as the internal calculation doesn't work

        var fb:FlowBox = event.target as FlowBox;

        if (fb != null)
        {
            if (fb.numChildren > 0)
            {
                // default the needed height to the top view metric

                var maxBottom:int = fb.viewMetrics.top;

                // Iterate over the children of the FlowBox to find the bottom-most bottom, so
                // we can determine how big / small we can make the FlowBox.
                // If it's a UIComponent, include it if the includeInLayout property is true.
                // If it's not a UIComponent, include it if the visible property is true.

                for (var idx:int = 0; idx < fb.numChildren; idx++)
                {
                    var displayObject:DisplayObject = fb.getChildAt(idx);

                    if ((displayObject is UIComponent && (displayObject as
                                                          UIComponent).includeInLayout) ||
                        (!(displayObject is UIComponent) && displayObject.visible))
                    {
                        var thisBottom:int = fb.getChildAt(idx).y + fb.getChildAt(idx).height;

                        if (thisBottom > maxBottom)
                            maxBottom = thisBottom;
                    }
                }

                fb.height = maxBottom + fb.viewMetrics.bottom +
                            fb.getStyle("paddingBottom") + 1;
            }
            else
            {
                fb.height = fb.viewMetrics.top + fb.viewMetrics.bottom;
            }
        }
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
            loadingFromReport = true;
            blah = true;
            _filterDefinitions = value;
            for each (var filterDefinition:FilterDefinition in _filterDefinitions) {
                addFilterDefinition(filterDefinition);
                /*filterMap[filter.filterDefinition.field.qualifiedName()] = filter;
                filterDefinitions.addItem(filter.filterDefinition);
                filter.analysisItems = _analysisItems;*/
            }
            loadingFromReport = false;
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

    public function set analysisItems(analysisItems:ArrayCollection):void {
        _analysisItems = analysisItems;
    }

    private var dropToolTip:ToolTip;

    public function showDropMessage():void {

        if (dropToolTip == null) {

            var point:Point = new Point();
            point.x = 0;
            point.y = 0;
            point = this.localToGlobal(point);
            var x:int = point.x + this.width / 2 - 100;
            var y:int = point.y + this.height / 2 - 15;

            dropToolTip = ToolTipManager.createToolTip("Drop Items Here to Filter", x, y) as ToolTip;
            dropToolTip.width = 250;
            dropToolTip.height = 35;
            dropToolTip.setStyle("fontSize", 18);
        }
    }

    public function closeDropMessage():void {
        if (dropToolTip != null) {
            ToolTipManager.destroyToolTip(dropToolTip);
            dropToolTip = null;
        }
    }

    override protected function createChildren():void {
        super.createChildren();
        if (filterTile == null) {
            filterTile = new FlowBox();
            filterTile.setStyle("horizontalGap", 20);
            filterTile.addEventListener(FlexEvent.UPDATE_COMPLETE, adapterFlowBoxUpdateCompleteHandler);
            filterTile.percentWidth = 100;
            filterTile.percentHeight = 100;
        }
        addChild(filterTile);
        if (noFilters && _filterDefinitions == null) {

        } else {
            if (_filterDefinitions != null) {
                for each (var filter:FilterDefinition in _filterDefinitions) {
                    addFilterDefinition(filter);
                }
            }
        }
    }

    private var _loadingFromReport:Boolean = false;

    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

    public function addFilterDefinition(filterDefinition:FilterDefinition):IFilter {
        var filter:IFilter = createFilter(filterDefinition);
        commandFilterAdd(filter);
        return filter;
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

    private function createFilter(filterDefinition:FilterDefinition):IFilter {
        var filter:IFilter;
        if (filterDefinition.getType() == FilterDefinition.VALUE) {
            var filterValueDefinition:FilterValueDefinition = filterDefinition as FilterValueDefinition;
            if (filterValueDefinition.inclusive && filterValueDefinition.filteredValues.length == 1) {
                filter = new ComboBoxFilter(_feedID, filterDefinition.field);
            } else {
                filter = new MultiValueFilter(_feedID, filterDefinition.field);
            }
        } else if (filterDefinition.getType() == FilterDefinition.DATE) {
            filter = new SliderDateFilter(_feedID, filterDefinition.field);
        } else if (filterDefinition.getType() == FilterDefinition.RANGE) {
            filter = new SliderMeasureFilter(_feedID, filterDefinition.field);
        } else if (filterDefinition.getType() == FilterDefinition.ROLLING_DATE) {
            filter = new RollingRangeFilter(_feedID, filterDefinition.field);
        } else if (filterDefinition.getType() == FilterDefinition.LAST_VALUE) {
            filter = new LastValueFilter(_feedID, filterDefinition.field);
        } else if (filterDefinition.getType() == FilterDefinition.PATTERN) {
            filter = new PatternFilter(_feedID, filterDefinition.field);
        }
        filter.filterEditable = _filterEditable;
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

    protected function dragEnterHandler(event:DragEvent):void {
        var analysisItem:AnalysisItem;
        if (event.dragInitiator is DataGrid) {
            var initialList:DataGrid = DataGrid(event.dragInitiator);
            analysisItem = AnalysisItemWrapper(initialList.selectedItem).analysisItem;
        } else if (event.dragInitiator is List) {
            var list:List = List(event.dragInitiator);
            analysisItem = list.selectedItem as AnalysisItem;
        } else if (event.dragInitiator is AdvancedDataGrid) {
            var analysisItemLabel:AdvancedDataGrid = event.dragInitiator as AdvancedDataGrid;
            var wrapper:AnalysisItemWrapper = analysisItemLabel.selectedItem as AnalysisItemWrapper;
            if (wrapper.isAnalysisItem()) {
                analysisItem = wrapper.analysisItem;
            }
        }
        if (analysisItem != null) {
            setStyle("borderThickness", 1);
            setStyle("borderStyle", "solid");
            setStyle("borderColor", "green");
            DragManager.acceptDragDrop(event.target as TransformContainer);
        }
    }

    protected function dragExitHandler(event:DragEvent):void {
        setStyle("borderThickness", 0);
        setStyle("borderStyle", "none");
        showingFeedback = false;
    }

    public function createNewFilter(analysisItem:AnalysisItem, stageX:int, stageY:int):void {
        UserAudit.instance().audit(UserAudit.CREATED_FILTER);
        if (analysisItem.hasType(AnalysisItemTypes.DATE)) {
            var window:DateFilterWindow = new DateFilterWindow();
            window.feedID = _feedID;
            window.item = analysisItem;
            window.addEventListener(FilterCreationEvent.FILTER_CREATION, onFilterSelection, false, 0, true);
            PopUpManager.addPopUp(window, this, true);
            //Alert.show(event.localX + " - " + event.localY);
            var x:int = stageX - 220;
            x = Math.max(x, 30);
            window.x = x;
            window.y = stageY - 35;
        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            var sliderMeasureFilter:SliderMeasureFilter = new SliderMeasureFilter(_feedID, analysisItem);
            initializeFilter(sliderMeasureFilter);
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            var dimWindow:GroupingFilterWindow = new GroupingFilterWindow();
            dimWindow.item = analysisItem;
            dimWindow.feedID = _feedID;
            dimWindow.addEventListener(FilterCreationEvent.FILTER_CREATION, onFilterSelection, false, 0, true);
            PopUpManager.addPopUp(dimWindow, this, true);
            dimWindow.x = Math.max(stageX - 220, 30);;
            dimWindow.y = stageY - 35;
        }
    }

    protected function dragDropHandler(event:DragEvent):void {
        setStyle("borderThickness", 0);
        setStyle("borderStyle", "none");
        showingFeedback = false;
        var analysisItem:AnalysisItem;
        if (event.dragInitiator is DataGrid) {
            var initialList:DataGrid = DataGrid(event.dragInitiator);
            analysisItem = AnalysisItemWrapper(initialList.selectedItem).analysisItem;
        } else if (event.dragInitiator is List) {
            var list:List = List(event.dragInitiator);
            analysisItem = list.selectedItem as AnalysisItem;
        } else if (event.dragInitiator is AdvancedDataGrid) {
            var analysisItemLabel:AdvancedDataGrid = event.dragInitiator as AdvancedDataGrid;
            var wrapper:AnalysisItemWrapper = analysisItemLabel.selectedItem as AnalysisItemWrapper;
            if (wrapper.isAnalysisItem()) {
                analysisItem = wrapper.analysisItem;
            }
        }
        createNewFilter(analysisItem, event.stageX, event.stageY);
    }

    private function onFilterSelection(event:FilterCreationEvent):void {
        initializeFilter(event.filter);
    }

    private function filterAdded(event:FilterUpdatedEvent):void {
        addFilter(event.filter);
        dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
    }

    private function filterUpdated(event:FilterUpdatedEvent):void {
        if (!filterDefinitions.contains(event.filterDefinition) || event.rebuild) {
            var index:int = filterDefinitions.getItemIndex(event.previousFilterDefinition);
            filterDefinitions.removeItemAt(index);
            var existingFilter:IFilter = null;
            for each (var filter:IFilter in filterTile.getChildren()) {
                if (filter.filterDefinition == event.previousFilterDefinition) {
                    existingFilter = filter;
                }
            }
            if (existingFilter != null) {
                filterTile.removeChild(existingFilter as DisplayObject);
            }
            var newFilter:IFilter = createFilter(event.filterDefinition);
            initializeFilter(newFilter);
        } else {
            dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
        }
    }

    public function commandFilterAdd(filter:IFilter):void {
        if (noFilters) {
            noFilters = false;
        }
        filter.filterEditable = _filterEditable;
        filter.loadingFromReport = _loadingFromReport;
        filter.showLabel = _showLabel;
        filter.addEventListener(FilterUpdatedEvent.FILTER_ADDED, filterAdded);
        filter.addEventListener(FilterUpdatedEvent.FILTER_UPDATED, filterUpdated);
        filter.addEventListener(FilterDeletionEvent.DELETED_FILTER, filterDeleted);
        filterTile.addChild(filter as DisplayObject);
        if (_loadingFromReport) {
            addFilter(filter);
        }
    }

    public function commandFilterAdd2(filter:IFilter):void {
        commandFilterAdd(filter);
        if (filter is SliderMeasureFilter) {
            SliderMeasureFilter(filter).edit(null);
        } else if (filter is PatternFilter) {
            PatternFilter(filter).edit(null);
        }
    }

    private function initializeFilter(filter:IFilter):void {
        dispatchEvent(new CommandEvent(new FilterAddCommand(this, filter)));
    }

    private function addFilter(filter:IFilter):void {
        filterMap[filter.filterDefinition.field.qualifiedName()] = filter;
        filterDefinitions.addItem(filter.filterDefinition);
        filter.analysisItems = _analysisItems;
    }

    public function getFilterDefinitions():ArrayCollection {
        return filterDefinitions;
    }

    public function commandFilterDelete(filter:IFilter):void {
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
        for each (var filter:IFilter in filterMap) {
            commandFilterDelete(filter);
        }
    }

    private function filterDeleted(event:FilterDeletionEvent):void {
        dispatchEvent(new CommandEvent(new FilterDeleteCommand(this, event.getFilter())));
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
            var filter:IFilter = filterMap[key.qualifiedName()];
            if (filter == null) {
                // doesn't exist yet, create it
                var filterDefinition:FilterDefinition;
                if (key.hasType(AnalysisItemTypes.DATE)) {
                    var filterDateRangeDefinition:FilterDateRangeDefinition = new FilterDateRangeDefinition();
                    filterDateRangeDefinition.field = key;
                    filterDefinition = filterDateRangeDefinition;
                    filter = new SliderDateFilter(_feedID, key);
                } else if (key.hasType(AnalysisItemTypes.DIMENSION)) {
                    var filterValueDefinition:FilterValueDefinition = new FilterValueDefinition();
                    filterValueDefinition.field = key;
                    filterValueDefinition.filteredValues = uniqueValues;
                    filterValueDefinition.inclusive = includeFilter;
                    filterDefinition = filterValueDefinition;
                    if (values.length == 1 && includeFilter) {
                        filter = new ComboBoxFilter(_feedID, key);
                    } else {
                        filter = new MultiValueFilter(_feedID, key);
                    }
                } else if (key.hasType(AnalysisItemTypes.MEASURE)) {
                    var filterMeasureRangeDefinition:FilterRangeDefinition = new FilterRangeDefinition();
                    filterMeasureRangeDefinition.field = key;
                    filterDefinition = filterMeasureRangeDefinition;
                    filter = new SliderMeasureFilter(_feedID, key);
                }
                filterMap[key.qualifiedName()] = filter;
                filter.filterDefinition = filterDefinition;
                initializeFilter(filter);
            } else {
                if (filter is MultiValueFilter) {
                    var multiValueFilter:MultiValueFilter = filter as MultiValueFilter;
                    // update the filtered values
                    if (!multiValueFilter.inclusive && includeFilter) {
                        multiValueFilter.toInclusive(uniqueValues);
                        // gonna change...
                    } else if (multiValueFilter.inclusive && !includeFilter) {
                        multiValueFilter.removeValues(uniqueValues);
                    } else {
                        // just changing values
                        multiValueFilter.addValues(uniqueValues);
                    }
                    dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
                } else if (filter is ComboBoxFilter) {

                } else if (filter is SliderDateFilter) {

                } else if (filter is RollingRangeFilter) {

                } else if (filter is SliderMeasureFilter) {

                }
            }

        }
    }
}
}