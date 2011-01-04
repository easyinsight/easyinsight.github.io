package com.easyinsight.api.v3;

/**
 * User: jamesboe
 * Date: 1/4/11
 * Time: 2:06 PM
 */
public class ResponseInfo {

    public static final int UNAUTHORIZED = 401;
    public static final int BAD_REQUEST = 400;
    public static final int SERVER_ERROR = 500;
    public static final int ALL_GOOD = 200;

    private int code;
    private String xml;

    public ResponseInfo(int code, String xml) {
        this.code = code;
        this.xml = xml;
    }

    public int getCode() {
        return code;
    }

    public String toResponse() {
        return "<response><code>" + code + "</code>" + xml + "</response>";
    }
}
