/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/18/11
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.analysis.list.ListDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.intention.ReportPropertiesIntention")]
public class ReportPropertiesIntention extends Intention {

    public var fullJoins:Boolean;
    public var summaryRow:Boolean;

    public function ReportPropertiesIntention() {
    }

    override public function apply(suggestionMetadata:SuggestionMetadata):void {
        if (fullJoins) {
            suggestionMetadata.report.fullJoins = true;
        }
        if (summaryRow) {
            ListDefinition(suggestionMetadata.report).summaryTotal = true;
        }
    }
}
}
