package com.easyinsight.datafeeds.google;

/**
 * User: jamesboe
 * Date: 12/17/10
 * Time: 12:00 PM
 */
public class QuickbaseResponse {
    private String errorMessage;
    private String sessionTicket;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSessionTicket() {
        return sessionTicket;
    }

    public void setSessionTicket(String sessionTicket) {
        this.sessionTicket = sessionTicket;
    }
}
