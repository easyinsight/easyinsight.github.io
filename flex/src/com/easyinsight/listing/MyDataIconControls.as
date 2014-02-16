package com.easyinsight.listing
{

import com.easyinsight.analysis.FilterSetDescriptor;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.datasources.DataSourceType;
import com.easyinsight.etl.LookupTableDescriptor;
import com.easyinsight.framework.DataFolder;
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.scorecard.ScorecardDescriptor;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.controls.LinkButton;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class MyDataIconControls extends UIComponent implements IListItemRenderer
{
    private var obj:Object;

    private var adminButton:LinkButton;

    private var _adminVisible:Boolean = true;
    private var _deleteVisible:Boolean = true;

    public function MyDataIconControls()
    {
        super();
    }

    override protected function createChildren():void {
        super.createChildren();

        if (adminButton == null) {
            adminButton = new LinkButton();
            adminButton.label = "Edit...";
            //adminButton.setStyle("icon", ImageConstants.EDIT_ICON);
            BindingUtils.bindProperty(adminButton, "visible", this, "adminVisible");
            adminButton.addEventListener(MouseEvent.CLICK, adminCalled);
        }
        addChild(adminButton);
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var buttonWidth:int = 80;
        var buttonHeight:int = 22;
        var padding:int = 5;
        if (adminButton != null) {
            adminButton.move((padding),0);
            adminButton.setActualSize(buttonWidth, buttonHeight);
        }
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

    [Bindable(event="deleteVisibleChanged")]
    public function get deleteVisible():Boolean {
        return _deleteVisible;
    }

    public function set deleteVisible(value:Boolean):void {
        if (_deleteVisible == value) return;
        _deleteVisible = value;
        dispatchEvent(new Event("deleteVisibleChanged"));
    }

    private function adminCalled(event:MouseEvent):void {
        if (obj is InsightDescriptor) {
            var analysisDefinition:InsightDescriptor = obj as InsightDescriptor;
            dispatchEvent(new AnalyzeEvent(new AnalysisDefinitionAnalyzeSource(analysisDefinition)));
        } else if (obj is DashboardDescriptor ){
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_EDITOR, {dashboardID: DashboardDescriptor(obj).id}))); 
        } else if (obj is ScorecardDescriptor ){
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.SCORECARD_EDITOR, {scorecardID: ScorecardDescriptor(obj).id})));
        } else if (obj is FilterSetDescriptor) {
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.FILTER_SET_EDITOR, {filterSetID:FilterSetDescriptor(obj).id})));
        }
    }

    [Bindable("dataChange")]
    public function set data(value:Object):void {
        this.obj = value;
        if (value is EIDescriptor) {
            var desc:EIDescriptor = value as EIDescriptor;
            deleteVisible = desc.role <= DataSourceType.EDITOR;
            if (value is InsightDescriptor) {
                adminVisible = true;
            } else if (value is LookupTableDescriptor) {
                adminVisible = false;
            } else if (value is DashboardDescriptor) {
                adminVisible = true;
            } else if (value is ScorecardDescriptor) {
                adminVisible = true;
            } else if (value is DataFolder) {
                deleteVisible = false;
                adminVisible = false;
            } else if (value is FilterSetDescriptor) {
                adminVisible = true;
            }
        }
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return this.obj;
    }
}
}