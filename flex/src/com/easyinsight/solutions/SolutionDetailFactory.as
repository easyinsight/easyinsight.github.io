package com.easyinsight.solutions {
import com.easyinsight.listing.IPerspective;

import mx.containers.Box;

public class SolutionDetailFactory {
    public function SolutionDetailFactory() {
    }

    public static function createDetailPage(solution:Solution, auth:Boolean):IPerspective {
        var page:IPerspective;
        switch (solution.solutionID) {
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
            default:
                var detail:RevisedSolutionDetail = new RevisedSolutionDetail();
                detail.solution = solution;
                page = detail;
        }        
        return page;
    }
}
}