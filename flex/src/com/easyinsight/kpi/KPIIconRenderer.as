package com.easyinsight.kpi {
import com.easyinsight.goals.*;
import com.easyinsight.analysis.PopupMenuFactory;
import com.easyinsight.kpi.KPI;
import com.easyinsight.util.PrefixManager;

import flash.events.ContextMenuEvent;
import flash.ui.ContextMenuItem;

import mx.containers.HBox;
import mx.controls.Image;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class KPIIconRenderer extends UIComponent implements IListItemRenderer {

    private var image:Image;

    public function KPIIconRenderer() {
        super();
        image = new Image();
        //this.percentWidth = 100;
        //this.percentHeight = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    private var kpi:KPI;

    [Bindable("dataChange")]    
    public function set data(val:Object):void {
        this.kpi = val as KPI;
        if (this.kpi.iconImage != null) {
            image.load(PrefixManager.prefix + "/app/assets/icons/32x32/" + this.kpi.iconImage);
        } else {
            image.source = null;
        }
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));        
    }

    public function get data():Object {
        return this.kpi;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        image.move(9,0);
        image.setActualSize(32, 32);
    }
}
}