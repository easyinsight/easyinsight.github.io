package com.easyinsight.solutions {
import com.easyinsight.listing.IPerspective;

import mx.containers.Box;

public class SolutionDetailFactory {
    public function SolutionDetailFactory() {
    }

    public static function createDetailPage(solution:Solution, auth:Boolean):IPerspective {
        var page:IPerspective;
        if (solution.solutionID == 12) {
            var gSol:GoogleAnalyticsSolution = new GoogleAnalyticsSolution();
            gSol.solution = solution;
            gSol.newAuth = auth;
            page = gSol;
        } else {
            var detail:RevisedSolutionDetail = new RevisedSolutionDetail();
            detail.solution = solution;
            page = detail;
        }
        return page;
    }
}
}