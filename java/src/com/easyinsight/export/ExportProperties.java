package com.easyinsight.export;

/**
 * User: jamesboe
 * Date: 10/5/12
 * Time: 8:37 AM
 */
public class ExportProperties {
    private boolean embedded;
    private boolean emailed;

    public ExportProperties() {

    }

    public ExportProperties(boolean emailed, boolean embedded) {
        this.emailed = emailed;
        this.embedded = embedded;
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
