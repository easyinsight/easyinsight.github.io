package com.easyinsight.kpi {
import com.easyinsight.goals.*;
import com.easyinsight.analysis.PopupMenuFactory;
import com.easyinsight.kpi.KPI;
import com.easyinsight.util.PrefixManager;

import flash.events.ContextMenuEvent;
import flash.ui.ContextMenuItem;

import mx.containers.HBox;
import mx.controls.Image;
public class KPIIconRenderer extends HBox{

    private var image:Image;

    public function KPIIconRenderer() {
        super();
        image = new Image();
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        this.percentHeight = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    private var kpi:KPI;

    override public function set data(val:Object):void {
        this.kpi = val as KPI;
        if (this.kpi.iconImage != null) {
            image.load(PrefixManager.prefix + "/app/assets/icons/32x32/" + this.kpi.iconImage);
        }
    }

    override public function get data():Object {
        return this.kpi;
    }
}
}