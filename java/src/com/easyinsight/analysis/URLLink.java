package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.calculations.Resolver;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
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

    public List<Key> neededKeys(Resolver resolver) {
        List<String> keyString = URLPattern.getKeys(url);
        List<Key> keys = new ArrayList<Key>();
        for (String string : keyString) {
            Key key = resolver.getKey(string);
            if (key != null) {
                keys.add(key);
            }
        }
        return keys;
    }

    public boolean generatesURL() {
        return true;
    }

    public String generateLink(IRow row, Map<String, String> dataSourceProperties) {
        return URLPattern.getURL(url, row, dataSourceProperties);
    }
}
