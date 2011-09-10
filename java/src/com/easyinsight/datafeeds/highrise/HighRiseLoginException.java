package com.easyinsight.datafeeds.highrise;

/**
 * User: James Boe
* Date: Jun 17, 2009
* Time: 9:34:16 AM
*/
class HighRiseLoginException extends Exception {

    private String detail;

    HighRiseLoginException() {
    }

    HighRiseLoginException(String message) {
        super(message);
    }

    HighRiseLoginException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}