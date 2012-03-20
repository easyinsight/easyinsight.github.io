/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/12/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.rowedit {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ActualRow")]
public class ActualRow {

    public var values:Object;
    public var rowID:int;

    public function ActualRow() {
    }
}
}