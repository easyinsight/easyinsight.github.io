package com.easyinsight.twitter;

/**
 * User: jamesboe
 * Date: Jul 31, 2009
 * Time: 1:56:51 PM
 */
public class Tweet {
    private String status;
    private String timeString;

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status + " - " + timeString;
    }
}
