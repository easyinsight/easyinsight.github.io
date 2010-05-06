package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIIconFactory;
import com.easyinsight.kpi.KPIOutcome;

import mx.containers.HBox;
import mx.controls.Image;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class KPIStatusRenderer extends UIComponent implements IListItemRenderer  {



    private var image:Image;

    public function KPIStatusRenderer() {
        super();
        image = new Image();
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }
    
    private var _kpi:KPI;

    [Bindable("dataChange")]
    public function set data(val:Object):void {
        this._kpi = val as KPI;
        image.source = KPIIconFactory.iconForKPI(_kpi);
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return this._kpi;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        image.move(8,8);
        image.setActualSize(16, 16);
    }
}
}