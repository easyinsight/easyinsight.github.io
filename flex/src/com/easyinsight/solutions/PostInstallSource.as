package com.easyinsight.solutions {
import com.easyinsight.framework.PerspectiveInfo;

public class PostInstallSource extends PerspectiveInfo {

    public function PostInstallSource(dataSourceDescriptor:DataSourceDescriptor) {
        super(PerspectiveInfo.POST_CONNECTION_INSTALL, new Object());
        properties.dataSourceDescriptor = dataSourceDescriptor;
    }
}
}