package com.easyinsight.dashboard {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.PopupMenuFactory;
import com.easyinsight.analysis.list.SizeOverrideEvent;
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.filtering.TransformContainer;
import com.easyinsight.filtering.TransformsUpdatedEvent;
import com.easyinsight.report.EmbedReportContextMenuFactory;
import com.easyinsight.report.EmbeddedReportExportWindow;
import com.easyinsight.report.ReportSetupEvent;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.SaveButton;

import flash.display.DisplayObject;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.managers.PopUpManager;

public class DashboardReportViewComponent extends VBox implements IDashboardViewComponent  {

    public var dashboardReport:DashboardReport;
    public var viewFactory:EmbeddedViewFactory;

    private var report:AnalysisDefinition;

    public function DashboardReportViewComponent() {
        super();
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        setStyle("verticalGap", 5);
        setStyle("paddingLeft", 5);
        setStyle("paddingRight", 5);
        setStyle("paddingTop", 5);
        setStyle("paddingBottom", 5);
    }

    public function stackPopulate(positions:DashboardStackPositions):void {
        if (positions != null) {
            positions.saveReport(dashboardReport.urlKey, dashboardReport.report);
            if (transformContainer != null) {
                for each (var filterDefinition:FilterDefinition in transformContainer.getFilterDefinitions()) {
                    var map:Object = positions.filterMap["r" + dashboardReport.urlKey];
                    if (map == null) {
                        map = new Object();
                        positions.filterMap["r" + dashboardReport.urlKey] = map;
                    }
                    map[filterDefinition.filterID] = filterDefinition;
                }
            }
        }
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo(dashboardReport.preferredWidth, alteredHeight == -1 ? dashboardReport.preferredHeight : alteredHeight, dashboardReport.autoCalculateHeight);
    }

    private var alteredHeight:int = -1;

    override protected function commitProperties():void {
        super.commitProperties();
        var sizeInfo:SizeInfo = obtainPreferredSizeInfo();
        if (sizeInfo.preferredWidth > 0) {
            width = dashboardReport.preferredWidth;
        } else {
            percentWidth = 100;
        }
        if (!sizeInfo.autoCalcHeight && sizeInfo.preferredHeight > 0) {
            height = dashboardReport.preferredHeight;
        } else if (!sizeInfo.autoCalcHeight && dashboardEditorMetadata.dashboard.absoluteSizing) {
            height = 400;
        } else if (!sizeInfo.autoCalcHeight) {
            percentHeight = 100;
        } else {
            percentHeight = NaN;
        }
        if (labelBox && !dashboardReport.showLabel) {
            removeChild(labelBox);
            labelBox = null;
        } else if (getChildren().length != 0 && !labelBox && dashboardReport.showLabel) {
            var blah:Box = new Box();
            blah.height = 24;
            blah.setStyle("backgroundColor", 0xDDDDDD);
            blah.setStyle("borderThickness", 1);
            blah.setStyle("borderStyle", "solid");
            blah.percentWidth = 100;
            blah.setStyle("horizontalAlign", "center");
            var label:Label = new Label();
            label.setStyle("fontSize", 14);
            label.text = dashboardReport.report.name;
            blah.addChild(label);
            addChildAt(blah, 0);
            labelBox = blah;
        }
    }

    private var labelBox:Box;

    public function onReportConfigure(report:InsightDescriptor):void {
        dashboardReport.report = report;
        if (transformContainer != null) {
            transformContainer.removeEventListener(TransformsUpdatedEvent.UPDATED_TRANSFORMS, transformsUpdated);
        }
        viewFactory.removeEventListener(ReportSetupEvent.REPORT_SETUP, onReportSetup);
        viewFactory.removeEventListener("export", onExport);
        viewFactory.removeEventListener(EmbeddedDataServiceEvent.DATA_RETURNED, onData);
        viewFactory.removeEventListener(SizeOverrideEvent.SIZE_OVERRIDE, sizeOverride);
        removeAllChildren();
        box = null;
        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(dashboardReport.report.reportType);
        var controller:IEmbeddedReportController = new controllerClass();
        viewFactory = controller.createEmbeddedView();
        viewFactory.styleCanvas = false;
        viewFactory.usePreferredHeight = dashboardReport.autoCalculateHeight;
        viewFactory.reportID = dashboardReport.report.id;
        //viewFactory.dataSourceID = dashboardEditorMetadata.dataSourceID;
        viewFactory.dataSourceID = dashboardReport.report.dataFeedID;
        viewFactory.dashboardID = dashboardEditorMetadata.dashboardID;
        viewFactory.reportPaddingWidth = dashboardEditorMetadata.dashboard.reportHorizontalPadding;
        viewFactory.spaceSides = false;
        if (dashboardReport.showLabel) {
            var blah:Box = new Box();
            blah.height = 28;
            blah.setStyle("backgroundColor", 0xDDDDDD);
            blah.setStyle("borderThickness", 1);
            blah.setStyle("borderStyle", "solid");
            blah.percentWidth = 100;
            blah.setStyle("horizontalAlign", "center");
            var label:LinkButton = new LinkButton();
            label.setStyle("fontSize", 14);
            label.label = dashboardReport.report.name;
            label.addEventListener(MouseEvent.CLICK, onLabelClick);
            blah.addChild(label);
            addChild(blah);
            labelBox = blah;
            addChild(viewFactory);
        } else {
            addChild(viewFactory);
        }

        viewFactory.addEventListener(ReportSetupEvent.REPORT_SETUP, onReportSetup);
        viewFactory.addEventListener("export", onExport);
        viewFactory.addEventListener(EmbeddedDataServiceEvent.DATA_RETURNED, onData);
        viewFactory.addEventListener(SizeOverrideEvent.SIZE_OVERRIDE, sizeOverride);
        queued = true;
        viewFactory.setup();
        if (dashboardEditorMetadata.fixedID) {
            viewFactory.contextMenu = new EmbedReportContextMenuFactory().createReportContextMenu(dashboardReport.report, viewFactory, this);
        } else {
            viewFactory.contextMenu = PopupMenuFactory.reportFactory.createReportContextMenu(dashboardReport.report, viewFactory, this);
        }
    }

    protected override function createChildren():void {
        super.createChildren();

        if (dashboardEditorMetadata.borderThickness == 0) {
            horizontalScrollPolicy = "off";
            verticalScrollPolicy = "off";
        }
        var sizeInfo:SizeInfo = obtainPreferredSizeInfo();
        if (sizeInfo.preferredWidth > 0) {
            width = dashboardReport.preferredWidth;
        } else {
            percentWidth = 100;
        }
        if (sizeInfo.preferredHeight > 0) {
            height = dashboardReport.preferredHeight;
        } else if (!sizeInfo.autoCalcHeight && dashboardEditorMetadata.dashboard.absoluteSizing) {
            height = 400;
        } else if (!sizeInfo.autoCalcHeight) {
            percentHeight = 100;
        } else {
            percentHeight = NaN;
        }

        /*if (dashboardEditorMetadata != null && dashboardEditorMetadata.retrievalState != null) {
            var custom:InsightDescriptor = dashboardEditorMetadata.retrievalState.getReport(dashboardReport.urlKey);
            if (custom != null) {
                dashboardReport.report = custom;
            }
        }*/

        /*if (dashboardEditorMetadata.borderThickness > 0) {
            setStyle("borderStyle", "inset");
            setStyle("borderThickness", 3);
            setStyle("borderColor", 0x00000);
        }*/
        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(dashboardReport.report.reportType);
        var controller:IEmbeddedReportController = new controllerClass();
        viewFactory = controller.createEmbeddedView();
        viewFactory.styleCanvas = false;
        viewFactory.usePreferredHeight = dashboardReport.autoCalculateHeight;
        viewFactory.reportID = dashboardReport.report.id;
        //viewFactory.dataSourceID = dashboardEditorMetadata.dataSourceID;
        viewFactory.dataSourceID = dashboardReport.report.dataFeedID;
        viewFactory.dashboardID = dashboardEditorMetadata.dashboardID;
        viewFactory.reportPaddingWidth = dashboardEditorMetadata.dashboard.reportHorizontalPadding;
        viewFactory.spaceSides = false;
        if (dashboardReport.showLabel) {
            var blah:Box = new Box();
            blah.height = 28;
            blah.setStyle("backgroundColor", 0xDDDDDD);
            blah.setStyle("borderThickness", 1);
            blah.setStyle("borderStyle", "solid");
            blah.percentWidth = 100;
            blah.setStyle("horizontalAlign", "center");
            var label:LinkButton = new LinkButton();
            label.setStyle("fontSize", 14);
            label.label = dashboardReport.report.name;
            label.addEventListener(MouseEvent.CLICK, onLabelClick);
            blah.addChild(label);
            addChild(blah);
            labelBox = blah;
            addChild(viewFactory);
        } else {
            addChild(viewFactory);
        }

        viewFactory.addEventListener(ReportSetupEvent.REPORT_SETUP, onReportSetup);
        viewFactory.addEventListener("export", onExport);
        viewFactory.addEventListener(EmbeddedDataServiceEvent.DATA_RETURNED, onData);
        viewFactory.addEventListener(SizeOverrideEvent.SIZE_OVERRIDE, sizeOverride);
        viewFactory.setup();
        if (dashboardEditorMetadata.fixedID) {
            viewFactory.contextMenu = new EmbedReportContextMenuFactory().createReportContextMenu(dashboardReport.report, viewFactory, this);
        } else {
            viewFactory.contextMenu = PopupMenuFactory.reportFactory.createReportContextMenu(dashboardReport.report, viewFactory, this);
        }

    }

    private function onCollapse(event:ExperimentEvent):void {
        removeChild(DisplayObject(event.currentTarget));
        box = null;
    }

    private var box:ExperimentBox;

    public function highlight():void {
        setStyle("borderStyle", "solid");
        setStyle("borderThickness", 1);
        setStyle("borderColor", 0);
    }

    public function unhighlight():void {
        setStyle("borderStyle", "none");
        setStyle("borderThickness", 0);
    }

    private function onLabelClick(event:MouseEvent):void {
        if (box == null) {
            box = new ExperimentBox();
            box.insightDescriptor = dashboardReport.report;
            box.viewFactory = viewFactory;
            box.dashboardReportViewComponent = this;
            var dashboardDescriptor:DashboardDescriptor = new DashboardDescriptor();
            dashboardDescriptor.id = dashboardEditorMetadata.dashboardID;
            dashboardDescriptor.urlKey = dashboardEditorMetadata.dashboard.urlKey;
            dashboardDescriptor.name = dashboardEditorMetadata.dashboard.name;
            dashboardDescriptor.dataSourceID = dashboardEditorMetadata.dashboard.dataSourceID;
            box.dashboardDescriptor = dashboardDescriptor;
            box.addEventListener(ExperimentEvent.EXPERIMENT_COLLAPSE, onCollapse, false, 0, true);
            addChildAt(box, 1);
        } else {
            removeChild(box);
            box = null;
        }
    }

    private function onExport(event:Event):void {
        viewFactory.updateExportMetadata();
        var window:EmbeddedReportExportWindow = new EmbeddedReportExportWindow();
        window.report = viewFactory.report;
        window.coreView = viewFactory.getChildAt(0);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function sizeOverride(event:SizeOverrideEvent):void {
        event.stopPropagation();
        if (event.height != -1) {
            this.percentHeight = NaN;
            var h:int = event.height + (this.transformContainer ? this.transformContainer.height : 0) + 25 + (dashboardReport.showLabel ? 30 : 0);
            this.height = h;
            alteredHeight = h;
            dispatchEvent(new SizeOverrideEvent(-1, h));
        }
    }


    private var filterMap:Object = new Object();

    public function updateAdditionalFilters(filterMap:Object):void {
        if (filterMap != null) {
            for (var id:String in filterMap) {
                var filters:Object = filterMap[id];
                if (filters != null) {
                    this.filterMap[id] = filters;
                }
            }
        }
    }

    private var setup:Boolean;

    private var queued:Boolean;

    private function onData(event:EmbeddedDataServiceEvent):void {
        this.report = event.analysisDefinition;
        dispatchEvent(new DashboardReportLoadEvent(DashboardReportLoadEvent.DASHBOARD_REPORT_LOAD));
    }

    private function createAdditionalFilters(filterMap:Object):ArrayCollection {
        var filterColl:ArrayCollection = new ArrayCollection();
        for (var id:String in filterMap) {
            var filters:Object = filterMap[id];
            if (filters != null) {
                var filterList:ArrayCollection = filters as ArrayCollection;
                for each (var filter:FilterDefinition in filterList) {
                    filterColl.addItem(filter);
                }
            }
        }
        return filterColl;
    }

    private var transformContainer:TransformContainer;

    public var elementID:String;

    private function transformsUpdated(event:Event):void {
        filterMap[elementID] = transformContainer.getFilterDefinitions();
        updateAdditionalFilters(filterMap);
        refresh();
    }

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    public function toggleFilters(showFilters:Boolean):void {
        if (hasFilters) {
            if (!showFilters) {
                if (transformContainer.parent) {
                    removeChild(transformContainer);
                }
            } else {
                if (!transformContainer.parent) {
                    addChildAt(transformContainer, dashboardReport.showLabel ? 1 : 0);
                }
            }
        }
    }

    private var hasFilters:Boolean = false;



    private function onReportSetup(event:ReportSetupEvent):void {
        viewFactory.registerPostProcessor(new ReportDashboardPostProcessor(dashboardEditorMetadata));
        var filterDefinitions:ArrayCollection = event.reportInfo.report.filterDefinitions;
        //viewFactory.filterDefinitions = filterDefinitions;
        if (event.reportInfo.report.filterDefinitions.length > 0) {

            var parentFilters:ArrayCollection = createAdditionalFilters(filterMap);
            transformContainer = new TransformContainer();
            transformContainer.report = event.reportInfo.report;

            transformContainer.filterEditable = false;
            var myFilterColl:ArrayCollection = new ArrayCollection();
            var visibleFilters:int = 0;
            for each (var filterDefinition:FilterDefinition in filterDefinitions) {
                var exists:Boolean = false;
                for each (var existing:FilterDefinition in parentFilters) {
                    if (existing.qualifiedName() == filterDefinition.qualifiedName()) {
                        exists = true;
                    }
                }
                if (exists) {
                    continue;
                }
                if (filterDefinition.showOnReportView) {
                    visibleFilters++;
                }
                myFilterColl.addItem(filterDefinition);
            }
            for each (var valFilter:FilterDefinition in myFilterColl) {
                if (dashboardEditorMetadata != null && dashboardEditorMetadata.retrievalState != null) {
                    dashboardEditorMetadata.retrievalState.forFilter(valFilter, "r" + dashboardReport.urlKey, dashboardReport.overridenFilters);
                }
            }
            var index:int = dashboardReport.showLabel ? 1 : 0;
            if (myFilterColl.length > 0) {

                hasFilters = true;
                transformContainer.feedID = dashboardEditorMetadata.dataSourceID;
                transformContainer.retrievalState = dashboardEditorMetadata.retrievalState;
                transformContainer.dashboardID = dashboardEditorMetadata.dashboardID;
                transformContainer.dashboard = dashboardEditorMetadata.dashboard;
                transformContainer.existingFilters = myFilterColl;
                filterMap[elementID] = myFilterColl;
                updateAdditionalFilters(filterMap);
                transformContainer.percentWidth = 100;
                transformContainer.setStyle("paddingLeft", 10);
                transformContainer.setStyle("paddingRight", 10);
                transformContainer.setStyle("paddingTop", 10);
                transformContainer.setStyle("paddingBottom", 10);
                transformContainer.filterStorageKey = "r" + dashboardReport.urlKey;
                transformContainer.reportView = true;
                transformContainer.role = dashboardEditorMetadata.role;
                transformContainer.addEventListener(TransformsUpdatedEvent.UPDATED_TRANSFORMS, transformsUpdated);
                if (visibleFilters > 0) {
                    addChildAt(transformContainer, index++);
                }
            }
        }
        if (event.reportInfo.report.adHocExecution) {
            var executeButton:SaveButton = new SaveButton();
            executeButton.label = "Run the Report";
            executeButton.addEventListener(MouseEvent.CLICK, runReport);
            var runBox:HBox = new HBox();
            runBox.setStyle("horizontalAlign", "center");
            runBox.percentWidth = 100;
            runBox.addChild(executeButton);
            addChildAt(runBox, index);
        }
        viewFactory.additionalFilterDefinitions = createAdditionalFilters(filterMap);
        setup = true;
        if (queued) {
            queued = false;
            retrievedDataOnce = true;
            if (event.reportInfo.report.manualButRunFirst) {
                viewFactory.forceRetrieve();
            } else {
                viewFactory.refresh();
            }
        }
    }

    private function runReport(event:MouseEvent):void {
        viewFactory.forceRetrieve();
    }

    public function refresh():void {
        if (setup) {
            retrievedDataOnce = true;
            viewFactory.additionalFilterDefinitions = createAdditionalFilters(filterMap);
            viewFactory.refresh();
        } else {
            queued = true;
        }
    }

    private var retrievedDataOnce:Boolean;

    public function initialRetrieve():void {
        if (dashboardEditorMetadata != null && !dashboardEditorMetadata.editor && dashboardEditorMetadata.transformContainer != null) {
            dashboardEditorMetadata.transformContainer.hideFilters(dashboardReport.dashboardFilterOverrides);
        }
        if (!retrievedDataOnce) {
            if (setup) {
                retrievedDataOnce = true;
                viewFactory.additionalFilterDefinitions = createAdditionalFilters(filterMap);
                if (report != null && report.manualButRunFirst) {
                    viewFactory.forceRetrieve();
                } else {
                    viewFactory.refresh();
                }

            } else {
                queued = true;
            }
        }
    }

    public function reportCount():ArrayCollection {
        var reports:ArrayCollection = new ArrayCollection();
        reports.addItem(report);
        return reports;
    }
}
}