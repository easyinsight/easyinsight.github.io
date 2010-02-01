package com.easyinsight.solutions {
import com.easyinsight.FullScreenPage;
import com.easyinsight.listing.AnalyzeSource;

public class PostInstallSource implements AnalyzeSource {

    private var dataSourceDescriptor:DataSourceDescriptor;
    private var solution:Solution;

    public function PostInstallSource(dataSourceDescriptor:DataSourceDescriptor, solution:Solution) {
        this.dataSourceDescriptor = dataSourceDescriptor;
        this.solution = solution;
    }

    public function createAnalysisPopup():FullScreenPage {
        var postInstallPage:PostInstallPage = new PostInstallPage();
        postInstallPage.dataSourceDescriptor = dataSourceDescriptor;
        postInstallPage.solution = solution;
        return postInstallPage;
    }
}
}