package com.easyinsight.solutions {
import com.easyinsight.framework.ModulePerspective;
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.listing.IPerspective;

public class SolutionDetailFactory {
    public function SolutionDetailFactory() {
    }

    public static function createDetailPage(solution:Solution, auth:Boolean):IPerspective {
        return new ModulePerspective(new PerspectiveInfo(PerspectiveInfo.CONNECTION_DETAIL, { solution: solution, auth: auth}));       
    }


}
}