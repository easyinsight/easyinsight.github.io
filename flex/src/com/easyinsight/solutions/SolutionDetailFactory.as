package com.easyinsight.solutions {
import com.easyinsight.listing.IPerspective;

import flash.utils.getDefinitionByName;

public class SolutionDetailFactory {
    public function SolutionDetailFactory() {
    }

    public static function createDetailPage(solution:Solution, auth:Boolean):IPerspective {
        var page:IPerspective;
        if (solution.detailPageClass != null && solution.detailPageClass != "") {
            var className:String = solution.detailPageClass;
            var clazz:Class = getDefinitionByName(className) as Class;
            page = new clazz();
            page["newAuth"] = auth;
            page["solution"] = solution;
        } else {
            var detail:RevisedSolutionDetail = new RevisedSolutionDetail();
            detail.solution = solution;
            page = detail;
        }
        return page;
    }

    private function blah():void {
        var a:GoogleAnalyticsSolutionDetail;
        var b:BasecampSolutionDetail;
        var c:HighriseSolutionDetail;
        var d:FileUploadSolutionDetail;
        var e:GoogleSpreadsheetSolutionDetail;
        var f:LinkedInSolutionDetail;
        var g:PivotalTrackerSolutionDetail;
        var h:SendGridSolutionDetail;
        var i:MySQLSolutionDetail;
    }
}
}