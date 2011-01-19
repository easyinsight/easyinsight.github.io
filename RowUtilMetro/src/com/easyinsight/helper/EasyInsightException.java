package com.easyinsight.helper;

/**
 * User: jamesboe
 * Date: 1/6/11
 * Time: 6:57 PM
 */
public class EasyInsightException extends RuntimeException {

    public EasyInsightException(String s) {
        super(s);
    }

    public EasyInsightException(Throwable throwable) {
        super(throwable);
    }
}
