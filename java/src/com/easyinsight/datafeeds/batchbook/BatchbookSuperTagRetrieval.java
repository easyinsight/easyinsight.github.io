package com.easyinsight.datafeeds.batchbook;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.datafeeds.FeedDefinition;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 4/17/12
 * Time: 10:34 AM
 */
public class BatchbookSuperTagRetrieval extends BatchbookBaseSource {
    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return null;
    }

    public Map<String, List<String>> getSuperTags(BatchbookCompositeSource batchbookCompositeSource) throws ParsingException {
        Map<String, List<String>> superTags = new HashMap<String, List<String>>();
        HttpClient httpClient = getHttpClient(batchbookCompositeSource.getBbApiKey(), "");
        Document doc = runRestRequest("/service/super_tags.xml", httpClient, new Builder(), batchbookCompositeSource.getUrl(), batchbookCompositeSource);
        Nodes superTagNodes = doc.query("/super_tags/super_tag");
        for (int i = 0; i < superTagNodes.size(); i++) {
            Node node = superTagNodes.get(i);
            String superTagName = queryField(node, "name/text()");
            Nodes fields = node.query("fields/field");
            List<String> fieldList = new ArrayList<String>();
            for (int j = 0; j < fields.size(); j++) {
                Element fieldNode = (Element) fields.get(j);
                String fieldType = fieldNode.getAttribute("type").getValue();
                String fieldName;
                Attribute fieldAttribute = fieldNode.getAttribute("name");
                if (fieldAttribute == null) {
                    fieldName = queryField(fieldNode, "text()");
                } else {
                    fieldName = fieldAttribute.getValue();
                }
                //if (fieldType.equals("Multiple choice")) {
                    fieldList.add(fieldName);
               // }
            }
            superTags.put(superTagName, fieldList);
        }
        /*
        <?xml version="1.0"?>
<super_tags type="array">
<super_tag>
  <name>supertag1</name>
  <fields>
    <field type="Multiple choice" name="Argh">
      <option>X</option>
      <option>Y</option>
      <option>Z</option>
    </field>
    <field type="Multiple choice" name="Argh2">
      <option>A</option>
      <option>B</option>
    </field>
  </fields>
</super_tag>
<super_tag>
  <name>social media</name>
  <fields>
    <field type="RSS/Feed Reader">Flickr Username</field>
    <field type="RSS/Feed Reader">Delicious Username</field>
    <field type="RSS/Feed Reader">Personal Blog</field>
    <field type="RSS/Feed Reader">Work Blog</field>
    <field type="RSS/Feed Reader">Linkedin Url</field>
    <field type="RSS/Feed Reader">Twitter Username</field>
    <field type="RSS/Feed Reader">Facebook Username</field>
    <field type="Multiple choice" name="Statuschoice">
      <option>A</option>
      <option>B</option>
      <option>C</option>
    </field>
  </fields>
</super_tag>
<super_tag>
  <name>tag 2</name>
  <fields>
    <field type="Multiple choice" name="Xyz">
      <option>ABC</option>
      <option>DEF</option>
    </field>
  </fields>
</super_tag>
</super_tags>

         */
        return superTags;
    }
}
