package com.easyinsight.export;

/**
 * User: jamesboe
 * Date: 10/5/12
 * Time: 8:37 AM
 */
public class ExportProperties {
    private boolean embedded;
    private boolean emailed;
    private String embedKey;
    private boolean includeHeaders = true;

    public ExportProperties() {

    }

    public ExportProperties(boolean emailed, boolean embedded, String embedKey) {
        this.emailed = emailed;
        this.embedded = embedded;
        this.embedKey = embedKey;
    }

    public boolean isIncludeHeaders() {
        return includeHeaders;
    }

    public void setIncludeHeaders(boolean includeHeaders) {
        this.includeHeaders = includeHeaders;
    }

    public String getEmbedKey() {
        return embedKey;
    }

    public void setEmbedKey(String embedKey) {
        this.embedKey = embedKey;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    public boolean isEmailed() {
        return emailed;
    }

    public void setEmailed(boolean emailed) {
        this.emailed = emailed;
    }
}
