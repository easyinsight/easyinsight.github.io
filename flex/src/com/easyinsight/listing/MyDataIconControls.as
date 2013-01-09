package com.easyinsight.listing
{

import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.datasources.DataSourceType;
import com.easyinsight.etl.LookupTableDescriptor;
import com.easyinsight.etl.LookupTableSource;
import com.easyinsight.framework.DataFolder;
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.scorecard.ScorecardDescriptor;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.controls.Button;
import mx.controls.LinkButton;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class MyDataIconControls extends UIComponent implements IListItemRenderer
{
    private var obj:Object;



    [Embed(source="../../../../assets/media_play_green.png")]
    public var playIcon:Class;



    private var adminButton:LinkButton;



    private var _analyzeTooltip:String = "Analyze...";
    private var _analyzeVisible:Boolean = true;
    private var _adminTooltip:String = "Administer...";
    private var _adminVisible:Boolean = true;
    private var _deleteTooltip:String = "Delete...";
    private var _deleteVisible:Boolean = true;

    public function MyDataIconControls()
    {
        super();
    }

    private var _showAnalyze:Boolean = true;
    private var _showAdmin:Boolean = true;
    private var _showDelete:Boolean = true;

    public function set showAnalyze(value:Boolean):void {
        _showAnalyze = value;
    }

    public function set showAdmin(value:Boolean):void {
        _showAdmin = value;
    }

    public function set showDelete(value:Boolean):void {
        _showDelete = value;
    }

    override protected function createChildren():void {
        super.createChildren();

        if (_showAdmin) {
            if (adminButton == null) {
                adminButton = new LinkButton();
                adminButton.label = "Edit...";
                //adminButton.setStyle("icon", ImageConstants.EDIT_ICON);
                BindingUtils.bindProperty(adminButton, "toolTip", this, "adminTooltip");
                BindingUtils.bindProperty(adminButton, "visible", this, "adminVisible");
                adminButton.addEventListener(MouseEvent.CLICK, adminCalled);
            }
            addChild(adminButton);
        }

        /*if (_showDelete) {
            if (deleteButton == null) {
                deleteButton = new Button();
                deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
                BindingUtils.bindProperty(deleteButton, "toolTip", this, "deleteTooltip");
                BindingUtils.bindProperty(deleteButton, "visible", this, "deleteVisible");
                deleteButton.addEventListener(MouseEvent.CLICK, deleteCalled);
            }
            addChild(deleteButton);
        }*/
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var buttonWidth:int = 80;
        var buttonHeight:int = 22;
        var padding:int = 5;
        var i:int = 1;
        if (adminButton != null) {
            adminButton.move((padding * i),0);
            adminButton.setActualSize(buttonWidth, buttonHeight);
            i++;
        }
        /*if (deleteButton != null) {
            deleteButton.move((padding * i) + (buttonWidth * (i - 1)),0);
            deleteButton.setActualSize(buttonWidth, buttonHeight);
            i++;
        }*/
    }

    [Bindable(event="analyzeTooltipChanged")]
    public function get analyzeTooltip():String {
        return _analyzeTooltip;
    }

    public function set analyzeTooltip(value:String):void {
        if (_analyzeTooltip == value) return;
        _analyzeTooltip = value;
        dispatchEvent(new Event("analyzeTooltipChanged"));
    }

    [Bindable(event="analyzeVisibleChanged")]
    public function get analyzeVisible():Boolean {
        return _analyzeVisible;
    }

    public function set analyzeVisible(value:Boolean):void {
        if (_analyzeVisible == value) return;
        _analyzeVisible = value;
        dispatchEvent(new Event("analyzeVisibleChanged"));
    }

    [Bindable(event="adminTooltipChanged")]
    public function get adminTooltip():String {
        return _adminTooltip;
    }

    public function set adminTooltip(value:String):void {
        if (_adminTooltip == value) return;
        _adminTooltip = value;
        dispatchEvent(new Event("adminTooltipChanged"));
    }

    [Bindable(event="adminVisibleChanged")]
    public function get adminVisible():Boolean {
        return _adminVisible;
    }

    public function set adminVisible(value:Boolean):void {
        if (_adminVisible == value) return;
        _adminVisible = value;
        dispatchEvent(new Event("adminVisibleChanged"));
    }

    [Bindable(event="deleteTooltipChanged")]
    public function get deleteTooltip():String {
        return _deleteTooltip;
    }

    public function set deleteTooltip(value:String):void {
        if (_deleteTooltip == value) return;
        _deleteTooltip = value;
        dispatchEvent(new Event("deleteTooltipChanged"));
    }

    [Bindable(event="deleteVisibleChanged")]
    public function get deleteVisible():Boolean {
        return _deleteVisible;
    }

    public function set deleteVisible(value:Boolean):void {
        if (_deleteVisible == value) return;
        _deleteVisible = value;
        dispatchEvent(new Event("deleteVisibleChanged"));
    }

    private function deleteCalled(event:MouseEvent):void {
        dispatchEvent(new DeleteDataSourceEvent(EIDescriptor(obj)));
    }

    private function analyzeCalled(event:MouseEvent):void {
        if (obj is InsightDescriptor) {
            var analysisDefinition:InsightDescriptor = obj as InsightDescriptor;
            dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(analysisDefinition)));
        } else if (obj is LookupTableDescriptor) {
            dispatchEvent(new AnalyzeEvent(new LookupTableSource(LookupTableDescriptor(obj).id)));
        } else if (obj is DashboardDescriptor ){
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_VIEW, {dashboardID: DashboardDescriptor(obj).id})));
        } else if (obj is ScorecardDescriptor ){
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.SCORECARD_VIEW, {scorecardID: ScorecardDescriptor(obj).id})));
        }
    }

    private function adminCalled(event:MouseEvent):void {
        if (obj is InsightDescriptor) {
            var analysisDefinition:InsightDescriptor = obj as InsightDescriptor;
            dispatchEvent(new AnalyzeEvent(new AnalysisDefinitionAnalyzeSource(analysisDefinition)));
        } else if (obj is DashboardDescriptor ){
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_EDITOR, {dashboardID: DashboardDescriptor(obj).id}))); 
        } else if (obj is ScorecardDescriptor ){
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.SCORECARD_EDITOR, {scorecardID: ScorecardDescriptor(obj).id})));
        }
    }

    [Bindable("dataChange")]
    public function set data(value:Object):void {
        this.obj = value;
        if (value is EIDescriptor) {
            var desc:EIDescriptor = value as EIDescriptor;
            analyzeVisible = true;
            deleteVisible = desc.role <= DataSourceType.EDITOR;
            if (value is InsightDescriptor) {
                adminVisible = true;
                adminTooltip = "Open report in the report editor...";
            } else if (value is LookupTableDescriptor) {
                adminVisible = false;
            } else if (value is DashboardDescriptor) {
                adminVisible = true;
            } else if (value is ScorecardDescriptor) {
                adminVisible = true;
            } else if (value is DataFolder) {
                deleteVisible = false;
                adminVisible = false;
                analyzeVisible = false;
            }
        }
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return this.obj;
    }
}
}