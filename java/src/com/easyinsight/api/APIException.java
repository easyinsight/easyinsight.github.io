package com.easyinsight.api;

/**
 * User: James Boe
 * Date: Feb 1, 2009
 * Time: 2:49:17 PM
 */
public class APIException extends Exception {

    public APIException(String apiFailure) {
        super(apiFailure);
    }
}
