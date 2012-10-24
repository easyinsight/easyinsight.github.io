package com.easyinsight.datafeeds.wufoo;

import com.easyinsight.datafeeds.FeedDefinition;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 10/22/12
 * Time: 5:00 PM
 */
public class WufooForms extends WufooBaseSource {
    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }

    private Map<String, WufooForm> forms = new HashMap<String, WufooForm>();

    public Map<String, WufooForm> getForms(WufooCompositeSource wufooCompositeSource) {
        try {
            HttpClient client = getHttpClient(wufooCompositeSource.getWfApiKey(), "x");
            Document doc = runRestRequest("/api/v3/forms.xml", client, wufooCompositeSource);
            Nodes formNodes = doc.query("/Forms/Form");
            for (int i = 0; i < formNodes.size(); i++) {
                Node formNode = formNodes.get(i);
                String name = formNode.query("Name/text()").get(0).getValue();
                String id = formNode.query("Hash/text()").get(0).getValue();
                forms.put(id, new WufooForm(id, name));
            }
            return forms;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        WufooCompositeSource wufooCompositeSource = new WufooCompositeSource();
        wufooCompositeSource.setUrl("easyinsight");
        wufooCompositeSource.setApiKey("5ZSC-98ML-7HB0-GS7Z");
        new WufooForms().getForms(wufooCompositeSource);
    }
}
