package com.easyinsight.groups
{
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.listing.*;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.report.PackageAnalyzeSource;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.reportpackage.ReportPackageDescriptor;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;

public class GroupMyDataIconControls extends HBox
{
    private var obj:Object;

    [Embed(source="../../../../assets/refresh.png")]
    public var refreshIcon:Class;

    [Embed(source="../../../../assets/businessman_edit.png")]
    public var adminIcon:Class;

    [Embed(source="../../../../assets/media_play_green.png")]
    public var playIcon:Class;

    private var adminButton:Button;
    private var analyzeButton:Button;

    private var _analyzeTooltip:String = "Analyze...";
    private var _analyzeVisible:Boolean = true;
    private var _adminTooltip:String = "Administer...";
    private var _adminVisible:Boolean = true;

    public function GroupMyDataIconControls()
    {
        super();
        analyzeButton = new Button();
        analyzeButton.setStyle("icon", playIcon);
        BindingUtils.bindProperty(analyzeButton, "toolTip", this, "analyzeTooltip");
        BindingUtils.bindProperty(analyzeButton, "visible", this, "analyzeVisible");
        analyzeButton.addEventListener(MouseEvent.CLICK, analyzeCalled);
        addChild(analyzeButton);        
        adminButton = new Button();
        adminButton.setStyle("icon", adminIcon);
        BindingUtils.bindProperty(adminButton, "toolTip", this, "adminTooltip");
        BindingUtils.bindProperty(adminButton, "visible", this, "adminVisible");
        adminButton.addEventListener(MouseEvent.CLICK, adminCalled);
        addChild(adminButton);

        this.addEventListener(RefreshNotificationEvent.REFRESH_NOTIFICATION, notifyRefresh);

        this.setStyle("paddingLeft", 5);
        this.setStyle("paddingRight", 5);
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

    private function notifyRefresh(event:RefreshNotificationEvent):void {
        Alert.show("Your data has begun to refresh and will be available when the refresh completes. This process may take some time depending on the size of the data source. You will receive a notification when the process is complete.");
    }

    private function analyzeCalled(event:MouseEvent):void {
        if (obj is DataFeedDescriptor) {
            var descriptor:DataFeedDescriptor = obj as DataFeedDescriptor;
            dispatchEvent(new AnalyzeEvent(new DescriptorAnalyzeSource(descriptor.id)));
        } else if (obj is InsightDescriptor) {
            var analysisDefinition:InsightDescriptor = obj as InsightDescriptor;
            dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(analysisDefinition)));
        } else if (obj is ReportPackageDescriptor) {
            var packageDescriptor:ReportPackageDescriptor = obj as ReportPackageDescriptor;
            dispatchEvent(new AnalyzeEvent(new PackageAnalyzeSource(packageDescriptor)));
        }
    }

    private function adminCalled(event:MouseEvent):void {
        if (obj is DataFeedDescriptor) {
            var descriptor:DataFeedDescriptor = obj as DataFeedDescriptor;
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DATA_SOURCE_ADMIN, {feedID: descriptor.id})));
        } else if (obj is InsightDescriptor) {
            var analysisDefinition:InsightDescriptor = obj as InsightDescriptor;
            dispatchEvent(new AnalyzeEvent(new AnalysisDefinitionAnalyzeSource(analysisDefinition)));
        }
    }

    override public function set data(value:Object):void {
        this.obj = value;
        if (value is DataFeedDescriptor) {
            adminVisible = false;            
        } else if (value is InsightDescriptor) {
            adminVisible = true;
            adminTooltip = "Open report in the report editor...";
        } else if (value is ReportPackageDescriptor) {
            adminVisible = true;
            adminTooltip = "Edit the package definition...";            
        }
    }

    override public function get data():Object {
        return this.obj;
    }
}
}