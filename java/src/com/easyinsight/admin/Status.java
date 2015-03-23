package com.easyinsight.admin;

import org.json.JSONException;
import org.json.JSONObject;

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
    private HealthInfo healthInfo;
    private String extendedMessage = "All Good!";
    private String extendedCode = "Success";
    private String publicDns;
    private String role;
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPublicDns() {
        return publicDns;
    }

    public void setPublicDns(String publicDns) {
        this.publicDns = publicDns;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExtendedMessage() {
        return extendedMessage;
    }

    public void setExtendedMessage(String extendedMessage) {
        this.extendedMessage = extendedMessage;
    }

    public String getExtendedCode() {
        return extendedCode;
    }

    public void setExtendedCode(String extendedCode) {
        this.extendedCode = extendedCode;
    }

    public HealthInfo getHealthInfo() {
        return healthInfo;
    }

    public void setHealthInfo(HealthInfo healthInfo) {
        this.healthInfo = healthInfo;
    }

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

    public JSONObject toJSON() throws JSONException {
        JSONObject jo;
        if(healthInfo != null) {
            jo = new JSONObject(healthInfo.toJSON().toString());
        } else {
            jo = new JSONObject();
        }

        jo.put("host", host);

        jo.put("message", message);
        jo.put("extended_message", extendedMessage);
        jo.put("code", code);
        jo.put("extended_code", extendedCode);
        jo.put("time", time);
        if(publicDns != null)
            jo.put("public_dns", publicDns);
        if(role != null)
            jo.put("role", role);
        return jo;
    }
}
