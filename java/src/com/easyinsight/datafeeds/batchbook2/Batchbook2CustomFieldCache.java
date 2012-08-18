package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.datafeeds.FeedDefinition;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 8/13/12
 * Time: 9:27 AM
 */
public class Batchbook2CustomFieldCache extends Batchbook2BaseSource {
    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }

    public Map<String, Batchbook2CustomFieldInfo> getCustomFieldSets(Batchbook2CompositeSource batchbookCompositeSource) {
        Map<String, Batchbook2CustomFieldInfo> infos = new HashMap<String, Batchbook2CustomFieldInfo>();
        HttpClient httpClient = Batchbook2BaseSource.getHttpClient(batchbookCompositeSource.getToken(), "");
        List customFields = (List) runRestRequest("/custom_field_sets.json", httpClient, batchbookCompositeSource).get("custom_field_sets");
        for (Object customFieldObject : customFields) {
            Map customField = (Map) customFieldObject;
            String customFieldID = customField.get("id").toString();
            String customFieldName = (String) customField.get("name");
            infos.put(customFieldID, new Batchbook2CustomFieldInfo(customFieldID, customFieldName));
        }
        return infos;
    }
}
