/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {

import com.easyinsight.analysis.LoadingModuleDisplay;
import com.easyinsight.analysis.charts.twoaxisbased.area.AreaChartModule;
import com.easyinsight.analysis.charts.twoaxisbased.line.LineChartModule;
import com.easyinsight.analysis.charts.xaxisbased.column.ColumnChartModule;
import com.easyinsight.analysis.charts.xaxisbased.column.StackedColumnChartModule;
import com.easyinsight.analysis.charts.yaxisbased.bar.BarChartModule;
import com.easyinsight.analysis.charts.yaxisbased.bar.StackedBarChartModule;
import com.easyinsight.analysis.crosstab.CrosstabModule;
import com.easyinsight.analysis.diagram.DiagramModule;
import com.easyinsight.analysis.gauge.GaugeModule;
import com.easyinsight.analysis.list.ListModule;
import com.easyinsight.analysis.tree.TreeModule;
import com.easyinsight.analysis.trend.TrendGridModule;
import com.easyinsight.analysis.trend.TrendModule;
import com.easyinsight.analysis.verticallist.CombinedVerticalListModule;
import com.easyinsight.analysis.verticallist.VerticalListModule;

import flash.events.Event;

import flash.events.EventDispatcher;

import flash.system.ApplicationDomain;

import mx.controls.Alert;
import mx.core.Container;

import mx.events.ModuleEvent;

import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class ReportModuleLoader extends EventDispatcher {

    private var moduleInfo:IModuleInfo;

    private var _loadingDisplay:LoadingModuleDisplay;
    private var container:Container;

    public function ReportModuleLoader() {
    }

    private var inline:Boolean = false;
    private var moduleName:String;

    public function loadReportRenderer(_reportRendererModule:String, container:Container):void {
        if (_reportRendererModule == "VerticalList.swf" || _reportRendererModule == "CombinedVerticalList.swf" ||
                _reportRendererModule == "CrosstabModule.swf" || _reportRendererModule == "ColumnChartModule.swf" ||
                _reportRendererModule == "BarChartModule.swf" || _reportRendererModule == "StackedColumnChartModule.swf" ||
                _reportRendererModule == "StackedBarChartModule.swf" || _reportRendererModule == "ListModule.swf" ||
                _reportRendererModule == "TreeModule.swf" || _reportRendererModule == "ListModule.swf" ||
                _reportRendererModule == "TrendGridModule.swf" || _reportRendererModule == "DiagramModule.swf" ||
                _reportRendererModule == "TrendModule.swf" || _reportRendererModule == "GaugeModule.swf" ||
                _reportRendererModule == "LineChartModule.swf" || _reportRendererModule == "AreaChartModule.swf") {
            inline = true;
            moduleName = _reportRendererModule;
            dispatchEvent(new Event("moduleLoaded"));
        } else {
            this.container = container;
            moduleInfo = ModuleManager.getModule(Constants.instance().prefix + "/app/"+Constants.instance().buildPath+"/" + _reportRendererModule);
            moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler);
            moduleInfo.addEventListener(ModuleEvent.ERROR, reportFailureHandler);
            _loadingDisplay = new LoadingModuleDisplay();
            _loadingDisplay.moduleInfo = moduleInfo;
            container.addChild(_loadingDisplay);
            moduleInfo.load(ApplicationDomain.currentDomain);
        }
    }

    private function reportLoadHandler(event:ModuleEvent):void {
        if (_loadingDisplay != null) {
            container.removeChild(_loadingDisplay);
            _loadingDisplay.moduleInfo = null;
            _loadingDisplay = null;
        }
        if (moduleInfo != null) {
            moduleInfo.removeEventListener(ModuleEvent.READY, reportLoadHandler);
            moduleInfo.removeEventListener(ModuleEvent.ERROR, reportFailureHandler);
        }
        dispatchEvent(new Event("moduleLoaded"));
    }

    public function create():Object {
        if (inline) {
            if (moduleName == "VerticalList.swf") {
                return new VerticalListModule();
            } else if (moduleName == "CombinedVerticalList.swf") {
                return new CombinedVerticalListModule();
            } else if (moduleName == "ColumnChartModule.swf") {
                return new ColumnChartModule();
            } else if (moduleName == "BarChartModule.swf") {
                return new BarChartModule();
            } else if (moduleName == "ListModule.swf") {
                return new ListModule();
            } else if (moduleName == "TreeModule.swf") {
                return new TreeModule();
            } else if (moduleName == "CrosstabModule.swf") {
                return new CrosstabModule();
            } else if (moduleName == "StackedColumnChartModule.swf") {
                return new StackedColumnChartModule();
            } else if (moduleName == "StackedBarChartModule.swf") {
                return new StackedBarChartModule();
            } else if (moduleName == "DiagramModule.swf") {
                return new DiagramModule();
            } else if (moduleName == "TrendModule.swf") {
                return new TrendModule();
            } else if (moduleName == "TrendGridModule.swf") {
                return new TrendGridModule();
            } else if (moduleName == "GaugeModule.swf") {
                return new GaugeModule();
            } else if (moduleName == "LineChartModule.swf") {
                return new LineChartModule();
            } else if (moduleName == "AreaChartModule.swf") {
                return new AreaChartModule();
            }
        }
        return moduleInfo.factory.create();
    }

    private function reportFailureHandler(event:ModuleEvent):void {
        if (event.errorText != "SWF is not a loadable module") {
            Alert.show(event.errorText);
        }
    }
}
}
