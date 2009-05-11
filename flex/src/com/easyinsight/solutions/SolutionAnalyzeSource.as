package com.easyinsight.solutions {
import com.easyinsight.FullScreenPage;
import com.easyinsight.listing.AnalyzeSource;

public class SolutionAnalyzeSource implements AnalyzeSource{

    private var solution:Solution;

    public function SolutionAnalyzeSource(solution:Solution) {
        super();
        this.solution = solution;
    }

    public function createAnalysisPopup():FullScreenPage {
        var solutionDetail:SolutionDetail = new SolutionDetail();
        solutionDetail.solution = solution;
        return solutionDetail;
    }
}
}