package com.easyinsight.api.dynamic;

import java.util.List;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 4:33:54 PM
 */
public class ClassDefinition {
    private List<MethodFactory> methods;
    private String name;

    public ClassDefinition(List<MethodFactory> methods, String name) {
        this.methods = methods;
        this.name = name;
    }

    public String toCode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("import java.util.*;\r\n");
        stringBuilder.append("import javax.jws.WebService;\r\n");
        stringBuilder.append("import com.easyinsight.api.dynamic.InboundData;\r\n\r\n");
        stringBuilder.append("import com.easyinsight.core.*;\r\n\r\n");
        stringBuilder.append("@WebService(targetNamespace=\"http://www.easy-insight.com/\", serviceName=\""+name+"\", portName=\""+name+"\", endpointInterface=\"IDynamicService\")\r\n");
        stringBuilder.append("public class DynamicService implements IDynamicService {\r\n");
        for (MethodFactory methodFactory : methods) {
            stringBuilder.append(methodFactory.toBody());
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
