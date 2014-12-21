package com.easyinsight.analysis;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Nov 3, 2010
 * Time: 7:32:48 PM
 */
public class ServerError extends ReportFault implements Serializable {
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

    @Override
    public String toString() {
        return message;
    }
}
