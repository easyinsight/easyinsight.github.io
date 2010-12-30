/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 12/29/10
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
import mx.containers.HBox;
import mx.controls.Image;

public class DataSourceHealthIndicator extends HBox {

    [Embed(source="../../../../assets/bullet_ball_green.png")]
    private var goodIcon:Class;

    [Embed(source="../../../../assets/bullet_triangle_yellow.png")]
    private var warningIcon:Class;

    [Embed(source="../../../../assets/bullet_square_glass_red.png")]
    private var errorIcon:Class;

    private var image:Image;

    private var activity:ScheduledActivity;

    public function DataSourceHealthIndicator() {
        image = new Image();
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    override public function set data(val:Object):void {
        activity = val as ScheduledActivity;
        if (activity.problemLevel == 0) {
            toolTip = null;
            image.source = goodIcon;
        } else if (activity.problemLevel == 1) {
            toolTip = activity.problemMessage;
            image.source = warningIcon;
        } else {
            toolTip = activity.problemMessage;
            image.source = errorIcon;
        }
    }
}
}
