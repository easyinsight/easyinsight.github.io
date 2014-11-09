package com.easyinsight.export;

/**
* User: jamesboe
* Date: 6/17/11
* Time: 9:11 PM
*/
public class AttachmentInfo {
    private byte[] body;
    private String name;
    private String encoding;
    private String inlineCID;

    AttachmentInfo(byte[] body, String name, String encoding) {
        this.body = body;
        this.name = name;
        this.encoding = encoding;
    }

    AttachmentInfo(byte[] body, String name, String encoding, String inlineCID) {
        this.body = body;
        this.name = name;
        this.encoding = encoding;
        this.inlineCID = inlineCID;
    }

    public String getInlineCID() {
        return inlineCID;
    }

    public byte[] getBody() {
        return body;
    }

    public String getName() {
        return name;
    }

    public String getEncoding() {
        return encoding;
    }
}
