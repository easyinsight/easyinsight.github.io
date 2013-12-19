package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.ReportKey;
import org.antlr.runtime.Token;

import java.util.List;
import java.util.Map;

public class VariableNode extends CalculationTreeNode {

    public VariableNode(Token t) {
        super(t);
    }

    /*public KeySpecification getVariableKey() {
        return variableKey;
    }*/

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public AggregateKey createAggregateKey() {
        return analysisItem.createAggregateKey();
    }

    public String toDisplay() {
        return analysisItem.toDisplay();
    }
    
    public void resolveVariableKey(Map<String, List<AnalysisItem>> keyItems, Map<String, List<AnalysisItem>> displayItems, Map<String, UniqueKey> namespaces) {
        String s;



        // if possible, this is where we need the parameter...
        String namespace = null;
        if (getChildCount() == 1) {
            s = getChild(0).getText().trim();
            if(s.startsWith("[") && s.endsWith("]"))
                s = s.substring(1, s.length() - 1);
        } else if (getChildCount() == 2) {
            s = getChild(1).getText().trim();
            if(s.startsWith("[") && s.endsWith("]"))
                s = s.substring(1, s.length() - 1);
            namespace = getChild(0).getText().trim();
            if (namespace.startsWith("[") && namespace.endsWith("]")) {
                namespace = namespace.substring(1, namespace.length() - 1);
            }
        } else {
            throw new RuntimeException();
        }

        //variableKey = new NamedKeySpecification(s);
        List<AnalysisItem> analysisItems = keyItems.get(s);

        if (analysisItems != null) {
            if (analysisItems.size() > 1) {
                if (namespace != null) {

                    UniqueKey uniqueKey = namespaces.get(namespace);
                    if (uniqueKey == null) {
                        throw new FunctionException("Could not resolve namespace " + namespace + ".");
                    }

                    AnalysisItem matchedTo = null;
                    for (AnalysisItem testItem : analysisItems) {
                        Key key = testItem.getKey();
                        if (key instanceof DerivedKey) {
                            DerivedKey derivedKey = (DerivedKey) key;
                            UniqueKey testKey = new UniqueKey(derivedKey.getFeedID(), UniqueKey.DERIVED);
                            if (testKey.equals(uniqueKey)) {
                                matchedTo = testItem;
                                break;
                            }
                        } else if (key instanceof ReportKey) {
                            ReportKey reportKey = (ReportKey) key;
                            UniqueKey testKey = new UniqueKey(reportKey.getReportID(), UniqueKey.REPORT);
                            if (testKey.equals(uniqueKey)) {
                                matchedTo = testItem;
                                break;
                            }
                        }
                    }

                    if (matchedTo == null) {
                        analysisItem = analysisItems.get(0);
                    } else {
                        analysisItem = matchedTo;
                    }
                }
                if (analysisItem == null) {
                    boolean matched = false;
                    for (AnalysisItem testItem : analysisItems) {
                        if (s.equals(testItem.toDisplay())) {
                            analysisItem = testItem;
                            matched = true;
                            break;
                        }
                    }
                    if (!matched) {
                        analysisItem = analysisItems.get(0);
                    }
                }
            } else {
                analysisItem = analysisItems.get(0);
            }
        } else {
            analysisItems = displayItems.get(s);
            if (analysisItems != null) {
                if (analysisItems.size() > 1) {
                    if (namespace != null) {


                        UniqueKey uniqueKey = namespaces.get(namespace);
                        if (uniqueKey == null) {
                            throw new FunctionException("Could not resolve namespace " + namespace + ".");
                        }

                        AnalysisItem matchedTo = null;
                        for (AnalysisItem testItem : analysisItems) {
                            Key key = testItem.getKey();
                            if (key instanceof DerivedKey) {
                                DerivedKey derivedKey = (DerivedKey) key;
                                UniqueKey testKey = new UniqueKey(derivedKey.getFeedID(), UniqueKey.DERIVED);
                                if (testKey.equals(uniqueKey)) {
                                    matchedTo = testItem;
                                    break;
                                }
                            } else if (key instanceof ReportKey) {
                                ReportKey reportKey = (ReportKey) key;
                                UniqueKey testKey = new UniqueKey(reportKey.getReportID(), UniqueKey.REPORT);
                                if (testKey.equals(uniqueKey)) {
                                    matchedTo = testItem;
                                    break;
                                }
                            }
                        }
                        if (matchedTo == null) {
                            analysisItem = analysisItems.get(0);
                        }
                    }


                }
                if (analysisItem == null) {
                    analysisItem = analysisItems.get(0);
                }
            }
        }
        if (analysisItem == null) {
            throw new FunctionException("We could not find a field named " + s + ".");
        }
    }

    public void resolveVariableKey(Map<String, List<AnalysisItem>> keyItems, Map<String, List<AnalysisItem>> displayItems, int aggregationType) {
        String s = getText().trim();
        if (s.startsWith("[") && s.endsWith("]"))
            s = s.substring(1, s.length() - 1);
        //variableKey = new AggregateKeySpecification(s, aggregationType);
        List<AnalysisItem> analysisItems = keyItems.get(s);
        if (analysisItems != null) {
            for (AnalysisItem item : analysisItems) {
                if (item.getType() == AnalysisItemTypes.MEASURE) {
                    AnalysisMeasure analysisMeasure = (AnalysisMeasure) item;
                    if (analysisMeasure.getAggregation() == aggregationType) {
                        analysisItem = item;
                        break;
                    } else {
                        AnalysisMeasure clonedMeasure;
                        try {
                            clonedMeasure = (AnalysisMeasure) analysisMeasure.clone();
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                        clonedMeasure.setAggregation(aggregationType);
                        analysisItem = clonedMeasure;
                        break;
                    }
                }
            }
        }
        if (analysisItem == null) {
            analysisItems = displayItems.get(s);
            if (analysisItems != null) {
                for (AnalysisItem item : analysisItems) {
                    if (item.getType() == AnalysisItemTypes.MEASURE) {
                        AnalysisMeasure analysisMeasure = (AnalysisMeasure) item;
                        if (analysisMeasure.getAggregation() == aggregationType) {
                            analysisItem = item;
                            break;
                        } else {
                            AnalysisMeasure clonedMeasure;
                            try {
                                clonedMeasure = (AnalysisMeasure) analysisMeasure.clone();
                            } catch (CloneNotSupportedException e) {
                                throw new RuntimeException(e);
                            }
                            clonedMeasure.setAggregation(aggregationType);
                            analysisItem = clonedMeasure;
                            break;
                        }
                    }
                }
            }
            if (analysisItem == null) {
                throw new FunctionException("We could not find a field named " + s);
            }
        }
    }

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
        visitor.visit(this);
    }

    private AnalysisItem analysisItem;
}
