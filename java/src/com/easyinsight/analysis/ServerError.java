package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: Nov 3, 2010
 * Time: 7:32:48 PM
 */
public class ServerError extends ReportFault {
    private String message;

    public ServerError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
