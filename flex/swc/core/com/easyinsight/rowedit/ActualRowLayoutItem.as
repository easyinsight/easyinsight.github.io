/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/4/12
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.rowedit {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ActualRowLayoutItem")]
public class ActualRowLayoutItem {

    public var columns:int;
    public var analysisItems:ArrayCollection;
    public var columnWidth:int;
    public var formLabelWidth:int;

    public function ActualRowLayoutItem() {
    }
}
}
