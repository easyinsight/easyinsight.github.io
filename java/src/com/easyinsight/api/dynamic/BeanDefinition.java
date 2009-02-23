package com.easyinsight.api.dynamic;

import com.easyinsight.analysis.AnalysisItem;

import java.util.List;

/**
 * User: James Boe
 * Date: Sep 7, 2008
 * Time: 11:11:43 PM
 */
public class BeanDefinition {
    private List<AnalysisItem> fields;
    private String beanName;

    public BeanDefinition(List<AnalysisItem> fields, String beanName) {
        this.fields = fields;
        this.beanName = beanName;
    }

    public String toCode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("import java.util.*;\r\n");
        stringBuilder.append("public class ");
        stringBuilder.append(beanName);
        stringBuilder.append(" {\r\n");
        for (AnalysisItem analysisItem : fields) {
            FieldFactory fieldFactory = new FieldFactory(analysisItem);
            stringBuilder.append(fieldFactory.toCode());
        }        
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
