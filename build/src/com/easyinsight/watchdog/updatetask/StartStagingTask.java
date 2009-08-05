package com.easyinsight.watchdog.updatetask;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Aug 5, 2009
 * Time: 3:20:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class StartStagingTask extends StartAppInstanceTask {
    @Override
    protected String getAMI() {
        return STAGING_AMI;
    }
}
