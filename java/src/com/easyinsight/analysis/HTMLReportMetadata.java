package com.easyinsight.analysis;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: jamesboe
 * Date: 8/12/12
 * Time: 12:00 PM
 */
public class HTMLReportMetadata {
    private int customHeight;
    private int verticalMargin;
    private boolean fullScreenHeight = true;
    private boolean embedded;
    private FilterHTMLMetadata filterMetadata;
    private boolean lazyFilter;

    public HTMLReportMetadata() {
    }

    public HTMLReportMetadata(int verticalMargin) {
        this.verticalMargin = verticalMargin;
    }

    public boolean isLazyFilter() {
        return lazyFilter;
    }

    public void setLazyFilter(boolean lazyFilter) {
        this.lazyFilter = lazyFilter;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    public FilterHTMLMetadata getFilterMetadata() {
        return filterMetadata;
    }

    public void setFilterMetadata(FilterHTMLMetadata filterMetadata) {
        this.filterMetadata = filterMetadata;
    }

    public JSONObject createStyleProperties() {
        try {
            JSONObject params = new JSONObject();
            if(customHeight > 0)
                params.put("customHeight", customHeight);
            if(verticalMargin > 0)
                params.put("verticalMargin", verticalMargin);
            return params;

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCustomHeight() {
        return customHeight;
    }

    public void setCustomHeight(int customHeight) {
        this.customHeight = customHeight;
    }

    public boolean isFullScreenHeight() {
        return fullScreenHeight;
    }

    public void setFullScreenHeight(boolean fullScreenHeight) {
        this.fullScreenHeight = fullScreenHeight;
    }
}
