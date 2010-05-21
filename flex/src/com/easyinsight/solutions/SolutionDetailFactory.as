package com.easyinsight.solutions {
import com.easyinsight.listing.IPerspective;

import flash.utils.getDefinitionByName;

import mx.containers.Box;

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
        /*switch (solution.solutionID) {
            case 12:
                var gSol:GoogleAnalyticsSolutionDetail = new GoogleAnalyticsSolutionDetail();
                gSol.solution = solution;
                gSol.newAuth = auth;
                page = gSol;
                break;
            case 8:
                var bSol:BasecampSolutionDetail = new BasecampSolutionDetail();
                bSol.solution = solution;
                page = bSol;
                break;
            case 19:
                var hSol:HighriseSolutionDetail = new HighriseSolutionDetail();
                hSol.solution = solution;
                page = hSol;
                break;
            case 1:
                var file:FileUploadSolutionDetail = new FileUploadSolutionDetail();
                file.solution = solution;
                page = file;
                break;
            case 3:
                var gsds:GoogleSpreadsheetSolutionDetail = new GoogleSpreadsheetSolutionDetail();
                gsds.solution = solution;
                page = gsds;
                break;
            default:
                var detail:RevisedSolutionDetail = new RevisedSolutionDetail();
                detail.solution = solution;
                page = detail;
        }*/
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

    }
}
}