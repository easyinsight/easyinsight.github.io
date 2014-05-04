package com.easyinsight.export;

/**
* User: jamesboe
* Date: 5/3/14
* Time: 2:25 PM
*/
public class ExportResponse {
    private byte[] bytes;
    private String urlKey;

    ExportResponse(byte[] bytes, String urlKey) {
        this.bytes = bytes;
        this.urlKey = urlKey;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getUrlKey() {
        return urlKey;
    }
}
