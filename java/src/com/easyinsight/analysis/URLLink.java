package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.calculations.Resolver;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Aug 28, 2009
 * Time: 10:44:15 AM
 */
@Entity
@Table(name="url_link")
@PrimaryKeyJoinColumn(name="link_id")
public class URLLink extends Link {
    @Column(name="url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<AnalysisItem> neededKeys(Map<String, List<AnalysisItem>> keyItems, Map<String, List<AnalysisItem>> displayItems) {
        List<String> keyString = URLPattern.getKeys(url);

        List<AnalysisItem> keys = new ArrayList<AnalysisItem>();
        for (String string : keyString) {
            AnalysisItem analysisItem = null;
            List<AnalysisItem> analysisItems = keyItems.get(string);
            if (analysisItems != null) {
                analysisItem = analysisItems.get(0);
            } else {
                analysisItems = displayItems.get(string);
                if (analysisItems != null) {
                    analysisItem = analysisItems.get(0);
                }
            }
            if (analysisItem != null) {
                keys.add(analysisItem);
            }
        }
        return keys;
    }

    public boolean generatesURL() {
        return true;
    }

    public String generateLink(IRow row, Map<String, String> dataSourceProperties, Collection<AnalysisItem> fields) {
        return URLPattern.getURL(url, row, dataSourceProperties, fields);
    }

    @Override
    public String toXML() {
        return "<urlLink url=\""+url + "\"/>";
    }
}
