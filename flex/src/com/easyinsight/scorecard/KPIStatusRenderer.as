package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIIconFactory;
import com.easyinsight.kpi.KPIOutcome;

import mx.containers.HBox;
import mx.controls.Image;

public class KPIStatusRenderer extends HBox {



    private var image:Image;

    public function KPIStatusRenderer() {
        super();
        image = new Image();
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }
    
    private var _kpi:KPI;

    override public function set data(val:Object):void {
        this._kpi = val as KPI;
        image.source = KPIIconFactory.iconForKPI(_kpi);
    }

    override public function get data():Object {
        return this._kpi;
    }
}
}