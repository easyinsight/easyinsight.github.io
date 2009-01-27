package com.easyinsight.api.dynamic;

import java.util.List;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 5:36:21 PM
 */
public class InterfaceDefinition {
    private List<MethodFactory> methods;
    private String name;

    public InterfaceDefinition(List<MethodFactory> methods, String name) {
        this.methods = methods;
        this.name = name;
    }

    public String toCode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("import java.util.*;\r\n");
        stringBuilder.append("import javax.jws.WebService;\r\n");
        stringBuilder.append("import javax.jws.WebParam;\r\n");
        stringBuilder.append("import com.easyinsight.api.dynamic.InboundData;\r\n\r\n");
        stringBuilder.append("import com.easyinsight.core.*;\r\n\r\n");
        stringBuilder.append("@WebService(name=\""+name+"\", targetNamespace=\"http://www.easy-insight.com/\")\r\n");
        stringBuilder.append("public interface IDynamicService {\r\n");
        for (MethodFactory methodFactory : methods) {
            stringBuilder.append("\t");
            stringBuilder.append(methodFactory.toDeclaration());
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
