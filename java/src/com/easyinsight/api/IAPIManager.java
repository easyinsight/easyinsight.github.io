package com.easyinsight.api;

import com.easyinsight.api.dynamic.DynamicDeploymentUnit;

/**
 * User: James Boe
 * Date: Feb 26, 2009
 * Time: 2:57:32 PM
 */
public interface IAPIManager {
    void start();

    void dynamicDeployment(DynamicDeploymentUnit unit);

    void undeploy(long feedID);
}
