package com.easyinsight.users;

import com.easyinsight.export.ExportMetadata;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: James Boe
 * Date: Apr 13, 2009
 * Time: 9:38:01 AM
 */
public class AccountSettings {
    private boolean apiEnabled;
    private String locale;
    private boolean publicData;
    private boolean marketplace;
    private boolean reportSharing;
    private long groupID;
    private int dateFormat;
    private String currencySymbol;
    private int firstDayOfWeek;
    private int maxResults;
    private boolean sendEmail;
    private boolean htmlView;

    public AccountSettings() {
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isHtmlView() {
        return htmlView;
    }

    public void setHtmlView(boolean htmlView) {
        this.htmlView = htmlView;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public int getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(int dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isPublicData() {
        return publicData;
    }

    public void setPublicData(boolean publicData) {
        this.publicData = publicData;
    }

    public boolean isMarketplace() {
        return marketplace;
    }

    public void setMarketplace(boolean marketplace) {
        this.marketplace = marketplace;
    }

    public boolean isReportSharing() {
        return reportSharing;
    }

    public void setReportSharing(boolean reportSharing) {
        this.reportSharing = reportSharing;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public boolean isApiEnabled() {
        return apiEnabled;
    }

    public void setApiEnabled(boolean apiEnabled) {
        this.apiEnabled = apiEnabled;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("date_format", dateFormat);
        jo.put("locale", locale);
        jo.put("currency_symbol", currencySymbol);
        jo.put("first_day_of_week", firstDayOfWeek);
        jo.put("max_rows", maxResults);
        jo.put("send_email", sendEmail);
        jo.put("html_view", htmlView);
        return jo;
    }

    public static AccountSettings fromJSON(net.minidev.json.JSONObject jo) {
        AccountSettings as = new AccountSettings();
        as.setDateFormat(Integer.valueOf(String.valueOf(jo.get("date_format"))));
        as.setLocale(String.valueOf(jo.get("locale")));
        as.setCurrencySymbol(String.valueOf(jo.get("currency_symbol")));
        as.setFirstDayOfWeek((Integer.valueOf(String.valueOf(jo.get("first_day_of_week")))));
        as.setMaxResults(Integer.valueOf(String.valueOf(jo.get("max_rows"))));
        as.setSendEmail(Boolean.valueOf(String.valueOf(jo.get("send_email"))));
        as.setHtmlView(Boolean.valueOf(String.valueOf(jo.get("html_view"))));
        return as;
    }
}