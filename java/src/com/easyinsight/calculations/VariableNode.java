package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.ReportKey;
import org.antlr.runtime.Token;

import java.util.*;

public class VariableNode extends CalculationTreeNode {

    private List<String> warnings;

    public VariableNode(Token t) {
        super(t);
    }

    /*public KeySpecification getVariableKey() {
        return variableKey;
    }*/

    public List<String> getWarnings() {
        return warnings;
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public AggregateKey createAggregateKey() {
        return analysisItem.createAggregateKey();
    }

    public FilterDefinition getFilterDefinition() {
        return filterDefinition;
    }

    public String toDisplay() {
        return analysisItem.toDisplay();
    }

    public void resolveVariableKey(Map<FilterKey, FilterDefinition> filterMap) {
        String s;
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
        if (namespace != null) {
            filterDefinition = filterMap.get(new FilterKey(s, namespace));
            if (filterDefinition == null) {
                throw new FunctionException("We could not find a filter named " + s + " on a report or dashboard named " + namespace + ".");
            }
        } else {
            for (FilterDefinition filterDefinition : filterMap.values()) {
                String label = filterDefinition.label(false);
                if (label != null && s.equals(label)) {
                    this.filterDefinition = filterDefinition;
                    break;
                }
            }
        }
        if (filterDefinition == null) {
            for (FilterDefinition filterDefinition : filterMap.values()) {
                if (filterDefinition.getField() != null) {
                    if (s.equals(filterDefinition.getField().toDisplay())) {
                        this.filterDefinition = filterDefinition;
                        break;
                    }
                }
            }
        }
        /*if (filterDefinition == null) {
            throw new FunctionException("We could not find a filter named " + s + ".");
        }*/
    }

    public void resolveVariableKey(Map<String, List<AnalysisItem>> keyItems, Map<String, List<AnalysisItem>> displayItems,
                                   Map<String, List<AnalysisItem>> unqualifiedDisplayItems, Map<String, UniqueKey> namespaces) {
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

        if (namespace != null) {

            UniqueKey uniqueKey = namespaces.get(namespace);
            if (uniqueKey == null) {
                throw new FunctionException("Could not resolve namespace " + namespace + ".");
            }

            List<AnalysisItem> subKeyItems = keyItems.get(s);
            List<AnalysisItem> subDisplayItems = displayItems.get(s);
            List<AnalysisItem> subUnqualifiedDisplayItems = unqualifiedDisplayItems.get(s);

            List<AnalysisItem> matchedByKey = new ArrayList<AnalysisItem>();
            Set<Key> matchedKeys = new HashSet<Key>();
            if (subKeyItems != null) {
                for (AnalysisItem testItem : subKeyItems) {
                    Key key = testItem.getKey();
                    if (key instanceof DerivedKey) {
                        DerivedKey derivedKey = (DerivedKey) key;

                        UniqueKey testKey = new UniqueKey(derivedKey.getFeedID(), UniqueKey.DERIVED);
                        if (testKey.equals(uniqueKey)) {
                            matchedKeys.add(key);
                            matchedByKey.add(testItem);
                            /*matchedTo = testItem;
                            break;*/
                        }
                    } else if (key instanceof ReportKey) {
                        ReportKey reportKey = (ReportKey) key;
                        UniqueKey testKey = new UniqueKey(reportKey.getReportID(), UniqueKey.REPORT);
                        if (testKey.equals(uniqueKey)) {
                            matchedKeys.add(key);
                            matchedByKey.add(testItem);
                            //matchedTo = testItem;
                            //break;
                        }
                    }
                }
            }
            if (subDisplayItems != null) {
                for (AnalysisItem testItem : subDisplayItems) {
                    Key key = testItem.getKey();
                    if (key instanceof DerivedKey) {
                        DerivedKey derivedKey = (DerivedKey) key;

                        UniqueKey testKey = new UniqueKey(derivedKey.getFeedID(), UniqueKey.DERIVED);
                        if (testKey.equals(uniqueKey) && !matchedKeys.contains(key)) {
                            matchedKeys.add(key);
                            matchedByKey.add(testItem);
                        }
                    } else if (key instanceof ReportKey) {
                        ReportKey reportKey = (ReportKey) key;
                        UniqueKey testKey = new UniqueKey(reportKey.getReportID(), UniqueKey.REPORT);
                        if (testKey.equals(uniqueKey) && !matchedKeys.contains(key)) {
                            matchedKeys.add(key);
                            matchedByKey.add(testItem);
                        }
                    }
                }
            }
            if (subUnqualifiedDisplayItems != null) {
                for (AnalysisItem testItem : subUnqualifiedDisplayItems) {
                    Key key = testItem.getKey();
                    if (key instanceof DerivedKey) {
                        DerivedKey derivedKey = (DerivedKey) key;

                        UniqueKey testKey = new UniqueKey(derivedKey.getFeedID(), UniqueKey.DERIVED);
                        if (testKey.equals(uniqueKey) && !matchedKeys.contains(key)) {
                            matchedByKey.add(testItem);
                        }
                    } else if (key instanceof ReportKey) {
                        ReportKey reportKey = (ReportKey) key;
                        UniqueKey testKey = new UniqueKey(reportKey.getReportID(), UniqueKey.REPORT);
                        if (testKey.equals(uniqueKey) && !matchedKeys.contains(key)) {
                            matchedByKey.add(testItem);
                        }
                    }
                }
            }
            if (matchedByKey.size() > 1) {
                boolean dupe = false;
                for (AnalysisItem testItem : matchedByKey) {
                    if (s.equals(testItem.toUnqualifiedDisplay())) {
                        if (analysisItem == null) {
                            analysisItem = testItem;
                        } else {
                            dupe = true;
                        }
                    }
                }
                if (analysisItem == null) {
                    if (warnings == null) {
                        warnings = new ArrayList<String>();
                    }

                    analysisItem = matchedByKey.get(0);
                    warnings.add("we found multiple fields matching the name of <b>" + s + "</b> and chose <b>" + analysisItem.toDisplay() + "</b>.");
                } else if (dupe) {
                    if (warnings == null) {
                        warnings = new ArrayList<String>();
                    }
                    warnings.add("we found multiple fields matching the name of <b>" + s + "</b> and chose <b>" + analysisItem.toDisplay() + "</b>.");
                }
            } else if (matchedByKey.size() == 1) {
                analysisItem = matchedByKey.get(0);
            }
        } else {
            if (analysisItems != null) {
                if (analysisItems.size() > 1) {
                    if (analysisItem == null) {
                        boolean matched = false;
                        boolean dupe = false;
                        for (AnalysisItem testItem : analysisItems) {
                            if (s.equals(testItem.toDisplay())) {
                                if (!matched) {
                                    analysisItem = testItem;
                                    matched = true;
                                } else {
                                    dupe = true;
                                }
                            }
                        }
                        if (!matched) {
                            if (warnings == null) {
                                warnings = new ArrayList<String>();
                            }
                            analysisItem = analysisItems.get(0);
                            warnings.add("we found multiple fields matching the name of <b>" + s + "</b> and chose <b>" + analysisItem.toDisplay() + "</b>.");
                        } else if (dupe) {
                            if (warnings == null) {
                                warnings = new ArrayList<String>();
                            }
                            warnings.add("we found multiple fields matching the name of <b>" + s + "</b> and chose <b>" + analysisItem.toDisplay() + "</b>.");
                        }
                    }
                } else {
                    analysisItem = analysisItems.get(0);
                }
            } else {
                analysisItems = displayItems.get(s);
                if (analysisItems != null) {
                    if (analysisItems.size() > 1) {
                        analysisItem = analysisItems.get(0);
                        if (warnings == null) {
                            warnings = new ArrayList<String>();
                        }
                        warnings.add("we found multiple fields matching the name of <b>" + s + "</b> and chose <b>" + analysisItem.toDisplay() + "</b>.");
                    }
                    if (analysisItem == null) {
                        analysisItem = analysisItems.get(0);
                    }
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

    private FilterDefinition filterDefinition;
}
