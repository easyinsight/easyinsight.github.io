/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/27/11
 * Time: 8:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import spark.components.Button;

public class DashboardButton extends Button {

    public var elementIndex:int;

    public function DashboardButton(elementIndex:int) {
        super();
        this.elementIndex = elementIndex;
    }
}
}
