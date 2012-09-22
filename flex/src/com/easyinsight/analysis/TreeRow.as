/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.TreeRow")]
public class TreeRow {

    public var values:Object;
    public var children:ArrayCollection;
    public var groupingColumn:Value;
    public var groupingField:AnalysisItem;
    public var summaryColumn:Boolean;

    public function TreeRow() {
    }
}
}
