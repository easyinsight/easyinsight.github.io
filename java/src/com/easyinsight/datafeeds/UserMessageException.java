package com.easyinsight.datafeeds;

/**
 * User: jamesboe
 * Date: Aug 11, 2010
 * Time: 11:55:34 AM
 */
public class UserMessageException extends RuntimeException {
    private String userMessage;

    public UserMessageException(Throwable throwable, String userMessage) {
        super(throwable);
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
