/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/17/11
 * Time: 10:02 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import mx.collections.ArrayCollection;
import mx.controls.Alert;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.OrFilter")]
public class OrFilter extends FilterDefinition {

    public var filters:ArrayCollection = new ArrayCollection();

    public function OrFilter() {
    }

    override public function getType():int {
        return FilterDefinition.OR;
    }

    override public function updateFromSaved(savedItemFilter:FilterDefinition):void {
        super.updateFromSaved(savedItemFilter);
        var orFilter:OrFilter = savedItemFilter as OrFilter;
        for each (var filter:FilterDefinition in filters) {
            for each (var savedFilter:FilterDefinition in orFilter.filters) {
                if (filter.matches(savedFilter)) {
                    filter.updateFromSaved(savedFilter);
                }
            }
        }
    }

    override public function updateFromReportView(editorFilter:FilterDefinition):void {
        super.updateFromReportView(editorFilter);
        var orFilter:OrFilter = editorFilter as OrFilter;
        for each (var filter:FilterDefinition in filters) {
            for each (var savedFilter:FilterDefinition in orFilter.filters) {
                if (filter.filterID == savedFilter.filterID) {
                    filter.updateFromReportView(savedFilter);
                }
            }
        }
    }
}
}
