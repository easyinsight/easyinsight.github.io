/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/20/13
 * Time: 5:56 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.administration.feed {
import com.easyinsight.analysis.ExtensionWrapWindow;
import com.easyinsight.analysis.ReportFieldExtension;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;

import mx.controls.LinkButton;
import mx.managers.PopUpManager;

public class ExtensionEditButton extends LinkButton {

    private var config:AnalysisItemConfiguration;
    public var configType:int;
    public var analysisItems:ArrayCollection;

    public function ExtensionEditButton() {
        addEventListener(MouseEvent.CLICK, onClick);

    }

    private function onClick(event:MouseEvent):void {
        var window:ExtensionWrapWindow = new ExtensionWrapWindow();
        window.config = config;
        window.reportType = configType;
        window.analysisItems = analysisItems;
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    override public function set data(val:Object):void {
        config = val as AnalysisItemConfiguration;
        var hasData:Boolean = false;
        if (configType == ReportFieldExtension.TEXT && config.textExtension) {
            hasData = true;
        } else if (configType == ReportFieldExtension.YTD && config.ytdExtension) {
            hasData = true;
        } else if (configType == ReportFieldExtension.VERTICAL_LIST && config.verticalListExtension) {
            hasData = true;
        }
        if (hasData) {
            this.label = "Edit...";
        } else {
            this.label = "Create...";
        }
    }

    override public function get data():Object {
        return config;
    }
}
}
