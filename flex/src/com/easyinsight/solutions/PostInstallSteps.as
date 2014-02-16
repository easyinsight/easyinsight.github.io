/**
 * Created by jamesboe on 12/8/13.
 */
package com.easyinsight.solutions {

import com.easyinsight.skin.ApplicationSkinTO;

[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.PostInstallSteps")]
public class PostInstallSteps {

    public var reloadStyling:Boolean;
    public var applicationSkin:ApplicationSkinTO;

    public function PostInstallSteps() {
    }
}
}
