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
    private String responseBody;

    public ResponseInfo(int code, String responseBody) {
        this.code = code;
        this.responseBody = responseBody;
    }

    public int getCode() {
        return code;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String toResponse() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><code>" + code + "</code>" + responseBody + "</response>";
    }
}
