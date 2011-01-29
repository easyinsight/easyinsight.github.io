package com.easyinsight.admin;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 1/28/11
 * Time: 11:16 PM
 */
public class Status implements Serializable {
    private long time;
    private String code;
    private String message;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
