/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/12/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.rowedit {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ActualRowSet")]
public class ActualRowSet {

    public var rows:ArrayCollection;
    public var analysisItems:ArrayCollection;
    public var dataSourceID:int;

    public function ActualRowSet() {
    }
}
}
