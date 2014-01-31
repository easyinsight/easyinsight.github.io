/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/21/14
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.FilterSet")]
public class FilterSet {

    public var filters:ArrayCollection;
    public var name:String;
    public var description:String;
    public var dataSourceID:int;
    public var urlKey:String;
    public var id:int;
    public var selected:Boolean;

    public function FilterSet() {
    }
}
}
