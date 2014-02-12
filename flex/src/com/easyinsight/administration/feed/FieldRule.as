/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/5/14
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AnalysisItemHandle;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.Link;
import com.easyinsight.analysis.ReportFieldExtension;
import com.easyinsight.analysis.TextReportFieldExtension;
import com.easyinsight.analysis.ytd.YTDReportFieldExtension;
import com.easyinsight.listing.Tag;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.FieldRule")]
public class FieldRule {

    public var link:Link;
    public var extension:ReportFieldExtension;

    public var explicitField:AnalysisItemHandle;
    public var tag:Tag;
    public var type:int;
    public var all:Boolean;
    public var selected:Boolean;

    public function FieldRule() {
    }

    public function get display():String {
        var str:String = "";
        if (tag != null) {
            str += "For all fields tagged " + tag.name + " ";
        } else if (explicitField != null) {
            str += ("For " + explicitField.name + " ");
        } else if (type == AnalysisItemTypes.DIMENSION) {
            str += "For all groupings ";
        } else if (type == AnalysisItemTypes.MEASURE) {
            str += "For all measures ";
        } else if (type == AnalysisItemTypes.DATE) {
            str += "For all dates ";
        } else {
            str += "WHYYYYYY";
        }
        if (link != null) {
            str += "add " + link.createString() + ".";
        } else if (extension != null) {
            var ext:ReportFieldExtension = extension;
            if (ext is YTDReportFieldExtension) {
                str += "configure YTD properties.";
            } else if (ext is TextReportFieldExtension) {
                str += "configure text properties."
            }
        }
        return str;
    }
}
}
