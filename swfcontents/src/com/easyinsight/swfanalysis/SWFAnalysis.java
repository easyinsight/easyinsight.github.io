package com.easyinsight.swfanalysis;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.util.*;

import com.easyinsight.dbservice.validated.*;

/**
 * User: James Boe
 * Date: Apr 12, 2009
 * Time: 6:29:01 PM
 */
public class SWFAnalysis extends Task {

    private String linkReportPath;
    private String key;
    private String secretKey;
    private String component;
    private String classAnalysisAPIKey;
    private String packageFilter;

    public void setClassAnalysisAPIKey(String classAnalysisAPIKey) {
        this.classAnalysisAPIKey = classAnalysisAPIKey;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setLinkReportPath(String linkReportPath) {
        this.linkReportPath = linkReportPath;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setPackageFilter(String packageFilter) {
        this.packageFilter = packageFilter;
    }

    private SizeInfo populateFromDependancies(ClassNode classNode, Map<ClassNode, List<ClassNode>> nodeMap, Set<ClassNode> processedHeap) {
        SizeInfo sizeInfo = new SizeInfo();
        processedHeap.add(classNode);
        sizeInfo.size = classNode.size;
        sizeInfo.optimizedSize = classNode.optimizedSize;
        List<ClassNode> dependancies = nodeMap.get(classNode);
        for (ClassNode node : dependancies) {
            if (!processedHeap.contains(node)) {
                processedHeap.add(node);
                SizeInfo childInfo = populateFromDependancies(classNode, nodeMap, processedHeap);
                sizeInfo.size += childInfo.size;
                sizeInfo.optimizedSize += childInfo.optimizedSize;
            }
        }
        return sizeInfo;
    }

    private static class SizeInfo {
        int size = 0;
        int optimizedSize = 0;
    }

    @Override
    public void execute() throws BuildException {
        validateProperties();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File file = new File(linkReportPath);
            Document document = documentBuilder.parse(file);
            NodeList scriptsList = document.getChildNodes();
            Node scriptsNode = scriptsList.item(0).getChildNodes().item(1);
            NodeList scriptList = scriptsNode.getChildNodes();
            StringPair componentPair = new StringPair("Component", component);
            Calendar cal = Calendar.getInstance();
            DatePair datePair = new DatePair("Date", cal);
            Map<ClassNode, List<ClassNode>> nodeMap = new HashMap<ClassNode, List<ClassNode>>();
            Map<ClassNode, ClassNode> containmentMap = new HashMap<ClassNode, ClassNode>();
            for (int i = 0; i < scriptList.getLength(); i++) {
                Node scriptNode = scriptList.item(i);
                if ("script".equals(scriptNode.getNodeName())) {
                    createRow(scriptNode, packageFilter, containmentMap, nodeMap);
                }
            }

            for (Map.Entry<ClassNode, List<ClassNode>> entry : nodeMap.entrySet()) {
                Set<ClassNode> processedHeap = new HashSet<ClassNode>();
                processedHeap.add(entry.getKey());
                int size = entry.getKey().size;
                int optimizedSize = entry.getKey().optimizedSize;
                for (ClassNode child : entry.getValue()) {
                    SizeInfo sizeInfo = populateFromDependancies(child, nodeMap, processedHeap);
                    size += sizeInfo.size;
                    optimizedSize += sizeInfo.optimizedSize;
                }
                entry.getKey().retainedSize = size;
                entry.getKey().retainedOptimizedSize = optimizedSize;
                System.out.println(entry.getKey().packageName + " - " + entry.getKey().name + " - " + size + " - " + optimizedSize);
            }
            
            BasicAuthValidatedPublish service = new BasicAuthValidatingPublishServiceServiceLocator().getBasicAuthValidatingPublishServicePort();
            ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setUsername(key);
            ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setPassword(secretKey);
            DayWhere dayWhere = new DayWhere();
            dayWhere.setKey("Date");
            dayWhere.setDayOfYear(cal.get(Calendar.DAY_OF_YEAR));
            dayWhere.setYear(cal.get(Calendar.YEAR));
            StringWhere componentWhere = new StringWhere();
            componentWhere.setKey("Component");
            componentWhere.setValue(component);

            Where where = new Where();
            where.setDayWheres(new DayWhere[] { dayWhere });
            where.setStringWheres(new StringWhere[] { componentWhere });
            List<Row> rows = new ArrayList<Row>();
            for (ClassNode classNode : nodeMap.keySet()) {
                StringPair namePair = new StringPair("Name", classNode.name);
                StringPair packagePair = new StringPair("Package", classNode.packageName);
                NumberPair sizePair = new NumberPair("Size", classNode.size);
                NumberPair optimizedSizePair = new NumberPair("Optimized Size", classNode.optimizedSize);
                NumberPair retainedSizePair = new NumberPair("Retained Size", classNode.retainedSize);
                NumberPair retainedOptimizedSizePair = new NumberPair("Retained Optimized Size", classNode.retainedOptimizedSize);
                Row row = new Row();
                row.setStringPairs(new StringPair[] { namePair, packagePair, componentPair });
                row.setNumberPairs(new NumberPair[] { sizePair, optimizedSizePair, retainedSizePair, retainedOptimizedSizePair });
                row.setDatePairs(new DatePair[] { datePair });
                rows.add(row);
            }
            Row[] rowArray = new Row[rows.size()];
            rows.toArray(rowArray);

            service.updateRows(classAnalysisAPIKey, rowArray, where);
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    private void validateProperties() throws BuildException {
        if (classAnalysisAPIKey == null) {
            throw new BuildException("classAnalysisAPIKey must be set to the API key of the data source you are publishing into.");
        }
        if (component == null) {
            throw new BuildException("You must set a component for the publish--for example, the Application or Module you are publishing results from.");
        }
        if (key == null) {
            throw new BuildException("You must set a key for the publish. You can find the key under Account/API Administration.");
        }
        if (secretKey == null) {
            throw new BuildException("You must set a secretKey for the publish. You can find the key under Account/API Administration.");
        }
        if (linkReportPath == null) {
            throw new BuildException("You must set a linkReportPath property. This property will match with the value used in the mxmlc ant task.");
        }
        if (packageFilter == null) {
            throw new BuildException("You must set a packageFilter property. This property defines your code as opposed to core mx classes in the Flex framework.");
        }
    }

    private void createRow(Node scriptNode, String filter, Map<ClassNode, ClassNode> containmentMap,
                          Map<ClassNode, List<ClassNode>> nodeMap) {
        NodeList detailNodes = scriptNode.getChildNodes();
        String name = null;
        String packageName = null;
        List<ClassNode> deps = new ArrayList<ClassNode>();
        for (int j = 0; j < detailNodes.getLength(); j++) {
            Node detailNode = detailNodes.item(j);
            if ("def".equals(detailNode.getNodeName())) {
                String id = detailNode.getAttributes().getNamedItem("id").getFirstChild().getNodeValue();
                String[] tokens = id.split(":");
                if (tokens.length > 1) {
                    packageName = tokens[0];
                    name = tokens[1];
                } else {
                    name = tokens[0];
                }
            } else if ("dep".equals(detailNode.getNodeName())) {
                String id = detailNode.getAttributes().getNamedItem("id").getFirstChild().getNodeValue();
                String depName;
                String depPackageName = null;
                if (id != null && id.contains(filter)) {
                    String[] tokens = id.split(":");
                    if (tokens.length > 1) {
                        depPackageName = tokens[0];
                        depName = tokens[1];
                    } else {
                        depName = tokens[0];
                    }
                    ClassNode newNode = new ClassNode(depPackageName, depName, 0, 0);
                    deps.add(newNode);
                }
            }
        }
        int size = Integer.parseInt(scriptNode.getAttributes().getNamedItem("size").getFirstChild().getNodeValue());
        int optimizedSize = Integer.parseInt(scriptNode.getAttributes().getNamedItem("optimizedsize").getFirstChild().getNodeValue());
        ClassNode newNode = new ClassNode(packageName, name, 0, 0);
        ClassNode classNode = containmentMap.get(newNode);
        List<ClassNode> edges;
        if (classNode == null) {
            edges = new ArrayList<ClassNode>();
            classNode = newNode;
            containmentMap.put(newNode, classNode);
            nodeMap.put(newNode, edges);
        } else {
            edges = nodeMap.get(classNode);
        }
        classNode.size = size;
        classNode.optimizedSize = optimizedSize;
        for (ClassNode depNode : deps) {
            ClassNode node = containmentMap.get(depNode);
            if (node == null) {
                node = depNode;
                containmentMap.put(depNode, depNode);
                nodeMap.put(depNode, new ArrayList<ClassNode>());
            }
            edges.add(node);
        }
    }

    private static class ClassNode {
        private String packageName;
        private String name;
        private int size;
        private int optimizedSize;
        private int retainedSize;
        private int retainedOptimizedSize;

        private ClassNode(String packageName, String name, int size, int optimizedSize) {
            this.packageName = packageName;
            this.name = name;
            this.size = size;
            this.optimizedSize = optimizedSize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassNode classNode = (ClassNode) o;

            if (name != null ? !name.equals(classNode.name) : classNode.name != null) return false;
            if (packageName != null ? !packageName.equals(classNode.packageName) : classNode.packageName != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = packageName != null ? packageName.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }
}
