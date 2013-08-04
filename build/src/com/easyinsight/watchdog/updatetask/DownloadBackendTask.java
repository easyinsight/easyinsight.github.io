package com.easyinsight.watchdog.updatetask;

/**
 * User: jamesboe
 * Date: 12/13/12
 * Time: 9:28 AM
 */
public class DownloadBackendTask extends DownloadAppInstanceTask {
    @Override
    protected String getAMI() {
        return APP_AMIS;
    }
}
