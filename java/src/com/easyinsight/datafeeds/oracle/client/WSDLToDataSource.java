package com.easyinsight.datafeeds.oracle.client;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 6/9/14
 * Time: 9:48 PM
 */
public class WSDLToDataSource {
    public static void main(String[] args) throws Exception {
        StringBuilder constants = new StringBuilder();
        StringBuilder fieldBuilder = new StringBuilder();
        StringBuilder dataSet = new StringBuilder();
        String targName = "com.easyinsight.datafeeds.oracle.client.Opportunity";
        Class clazz = Class.forName(targName);
        String[] tokens = targName.split("\\.");
        String actualName = tokens[tokens.length - 1];
        Opportunity opportunity;
        Map<String, String> childTypes = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            String name = field.getName();
            System.out.println(name);
            Class fieldType = field.getType();

            System.out.println("\t" + fieldType.getName());
            String baseType;
            String elName = fieldType.getName();
            Annotation annotation = field.getAnnotations()[0];
            if (annotation instanceof XmlElementRef) {
                XmlElementRef xmlElement = (XmlElementRef) annotation;
                elName = xmlElement.name();
            } else if (annotation instanceof XmlElement) {
                XmlElement xmlElement = (XmlElement) annotation;
                elName = xmlElement.name();
            }

            if (fieldType.getName().equals("javax.xml.bind.JAXBElement")) {
                String genericType = field.getGenericType().getTypeName();
                String extract = genericType.substring(fieldType.getName().length() + 1, genericType.length() - 1);
                System.out.println("\t\t" + extract);
                baseType = extract;
            } else if (fieldType.getName().equals("java.util.List")) {
                String genericType = field.getGenericType().getTypeName();
                String extract = genericType.substring(fieldType.getName().length() + 1, genericType.length() - 1);
                childTypes.put(elName, extract);
                continue;
            } else {
                baseType = field.getType().getName();
            }

            System.out.println("final name = " + baseType);
            String dataObject = actualName.toLowerCase();
            if (baseType.equals("java.lang.String")) {
                constants.append("\tpublic static final String ").append(name.toUpperCase()).append(" = \"").append(elName).append("\";\n");
                fieldBuilder.append("fieldBuilder.addField(").append(name.toUpperCase()).append(", new AnalysisDimension());\n");
                String getterName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                if (!fieldType.getName().equals("javax.xml.bind.JAXBElement")) {
                    dataSet.append("\t\trow.addValue(keys.get(").append(name.toUpperCase()).append("), ").append(dataObject).append(".get").append(getterName).append("());\n");
                } else {
                    dataSet.append("\t\trow.addValue(keys.get(").append(name.toUpperCase()).append("), ").append(dataObject).append(".get").append(getterName).append("().getValue());\n");
                }
            } else if (actualName.toLowerCase().endsWith("id")) {
                constants.append("\tpublic static final String ").append(name.toUpperCase()).append(" = \"").append(elName).append("\";\n");
                fieldBuilder.append("fieldBuilder.addField(").append(name.toUpperCase()).append(", new AnalysisDimension());\n");
                String getterName = Character.toUpperCase(name.charAt(0)) + name.substring(1);

                if (!fieldType.getName().equals("javax.xml.bind.JAXBElement")) {
                    dataSet.append("\t\trow.addValue(keys.get(").append(name.toUpperCase()).append("), String.valueOf(").append(dataObject).append(".get").append(getterName).append("()));\n");
                } else {
                    dataSet.append("\t\trow.addValue(keys.get(").append(name.toUpperCase()).append("), String.valueOf(").append(dataObject).append(".get").append(getterName).append("().getValue()));\n");
                }
            } else if (baseType.equals("java.math.BigDecimal")) {
                constants.append("\tpublic static final String ").append(name.toUpperCase()).append(" = \"").append(elName).append("\";\n");
                fieldBuilder.append("fieldBuilder.addField(").append(name.toUpperCase()).append(", new AnalysisMeasure());\n");
                String getterName = Character.toUpperCase(name.charAt(0)) + name.substring(1);

                if (fieldType.getName().equals("javax.xml.bind.JAXBElement")) {
                    dataSet.append("\t\trow.addValue(keys.get(").append(name.toUpperCase()).append("), getMeasureValue(").append(dataObject).append(".get").append(getterName).append("()));\n");
                } else {
                    dataSet.append("\t\trow.addValue(keys.get(").append(name.toUpperCase()).append("), ((java.math.BigDecimal)").append(dataObject).append(".get").append(getterName).append("()).doubleValue());\n");
                }
            } else if (baseType.equals("javax.xml.datatype.XMLGregorianCalendar")) {
                constants.append("\tpublic static final String ").append(name.toUpperCase()).append(" = \"").append(elName).append("\";\n");
                fieldBuilder.append("fieldBuilder.addField(").append(name.toUpperCase()).append(", new AnalysisDateDimension());\n");
                String getterName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                dataSet.append("\t\trow.addValue(keys.get(").append(name.toUpperCase()).append("), getDate(").append(dataObject).append(".get").append(getterName).append("()));\n");
            }
        }
        StringBuilder file = new StringBuilder();
        file.append("package com.easyinsight.datafeeds.oracle;\n\n");
        file.append("import com.easyinsight.analysis.AnalysisDimension;\n" +
                "import com.easyinsight.analysis.AnalysisMeasure;\n" +
                "import com.easyinsight.analysis.AnalysisDateDimension;\n" +
                "import com.easyinsight.analysis.IRow;\n" +
                "import com.easyinsight.analysis.ReportException;\n" +
                "import com.easyinsight.core.Key;\n" +
                "import com.easyinsight.database.EIConnection;\n" +
                "import com.easyinsight.datafeeds.FeedDefinition;\n" +
                "import com.easyinsight.datafeeds.FeedType;\n" +
                "import com.easyinsight.datafeeds.oracle.client.*;\n" +
                "import com.easyinsight.dataset.DataSet;\n" +
                "import com.easyinsight.logging.LogClass;\n" +
                "import com.easyinsight.storage.IDataStorage;\n" +
                "\n" +
                "import javax.xml.ws.BindingProvider;\n" +
                "import java.net.URL;\n" +
                "import java.sql.Connection;\n" +
                "import java.util.Date;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n\n");
        file.append("public class Oracle").append(actualName).append("Source").append(" extends OracleBaseSource {\n");
        file.append("public Oracle").append(actualName).append("Source() {\n" +
                "        setFeedName(\""+actualName+"\");\n" +
                "    }\n\n");
        file.append(constants);
        file.append("\n");
        file.append("protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {");
        file.append(fieldBuilder);
        file.append("}\n\n");
        file.append("@Override\n" +
                "    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {\n");
        file.append("\ttry {\n");
        for (Map.Entry<String, String> entry : childTypes.entrySet()) {
            file.append("\t\tList<" + entry.getValue() + "> " + entry.getKey() + "List = new ArrayList<>()");
        }
        file.append("\t\tDataSet dataSet = new DataSet();\n");
        file.append("OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;\n" +
                "            OpportunityService opportunityService = new OpportunityService_Service(new URL(oracleDataSource.getUrl() + \"/opptyMgmtOpportunities/OpportunityService?wsdl\")).getOpportunityServiceSoapHttpPort();\n" +
                "            BindingProvider prov = (BindingProvider) opportunityService;\n" +
                "            prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, oracleDataSource.getOracleUserName());\n" +
                "            prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, oracleDataSource.getOraclePassword());\n" +
                "            FindCriteria findCriteria = new FindCriteria();\n" +
                "            findCriteria.setFetchSize(1000);\n" +
                "            findCriteria.setFetchStart(0);\n" +
                "            FindControl findControl = new FindControl();\n" +
                "            List<Opportunity> opportunities = opportunityService.findOpportunity(findCriteria, findControl);");
        file.append("\t\tfor (Opportunity opportunity : opportunities) {\n");
        file.append("\t\tIRow row = dataSet.createRow();\n");
        file.append(dataSet);
        for (Map.Entry<String, String> entry : childTypes.entrySet()) {
            file.append("\t\t" + "for (" + entry.getValue() + " child : opportunity.get" + entry.getKey() + "()) {\n");
            file.append("\t\t\t" + entry.getKey() + "List.add(child);\n");
            file.append("\t\t}\n\n");
        }
        file.append("\t\t}\n\n");
        file.append("\t\treturn dataSet;\n");
        file.append("\t} catch (Exception e) {\n" +
                "            LogClass.error(e);\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n");
        file.append("}\n\n");
        file.append("@Override\n" +
                "    public FeedType getFeedType() {\n" +
                "        return FeedType.ORACLE_OPPORTUNITY;\n" +
                "    }\n\n");
        file.append("}");

        System.out.println(file.toString());

       /* for (Map.Entry<String, String> entry : childTypes.entrySet()) {
            System.out.println(entry.getKey() + " has " + entry.getValue());


        }*/
    }
}
