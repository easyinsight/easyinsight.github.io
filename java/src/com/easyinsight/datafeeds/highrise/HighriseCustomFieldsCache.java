package com.easyinsight.datafeeds.highrise;

import com.easyinsight.datafeeds.FeedDefinition;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * User: jamesboe
 * Date: 5/2/11
 * Time: 12:21 PM
 */
public class HighriseCustomFieldsCache extends HighRiseBaseSource {

    private Map<String, String> customFields = new HashMap<String, String>();

    public void blah(HttpClient client, String url, FeedDefinition parentDefinition, Date lastTime, HighriseCache highriseCache) throws HighRiseLoginException, ParsingException {
        Builder builder = new Builder();
        Document recordingsDoc = runRestRequest("/subject_fields.xml", client, builder, url, true, false, parentDefinition);
        Nodes customFieldNodes = recordingsDoc.query("/subject-fields/subject-field");
        for (int i = 0; i < customFieldNodes.size(); i++) {
            Node customFieldNode = customFieldNodes.get(i);
            String id = queryField(customFieldNode, "id/text()");
            String label = queryField(customFieldNode, "label/text()");
            customFields.put(id, label);
        }
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }
}
