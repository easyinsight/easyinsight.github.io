package com.easyinsight.export;

import com.easyinsight.analysis.AnalysisDateDimension;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 10/15/12
 * Time: 9:40 AM
 */
public class ReportDeliveryAudit {
    private String email;
    private int code;
    private String message;
    private Date date;

    public ReportDeliveryAudit() {
    }

    public ReportDeliveryAudit(String email, int code, String message, Date date) {
        this.email = email;
        this.code = code;
        this.message = message;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        DateFormat dateFormat = ExportService.getDateFormatForAccount(AnalysisDateDimension.MINUTE_LEVEL, null, md.dateFormat);
        JSONObject jo = new JSONObject();
        jo.put("email", getEmail());
        jo.put("message", getMessage());
        jo.put("code", getCode());
        jo.put("date", dateFormat.format(getDate()));
        return jo;
    }
}
