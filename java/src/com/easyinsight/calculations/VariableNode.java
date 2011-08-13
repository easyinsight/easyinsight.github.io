package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import org.antlr.runtime.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class VariableNode extends CalculationTreeNode {

	public VariableNode(Token t){
		super(t);                
    }

    public KeySpecification getVariableKey() {
        return variableKey;
    }

    public AggregateKey createAggregateKey() {
        return analysisItem.createAggregateKey();
    }
    
    public void resolveVariableKey(Map<String, List<AnalysisItem>> keyItems, Map<String, List<AnalysisItem>> displayItems) {
        String s = getText().trim();
        if(s.startsWith("[") && s.endsWith("]"))
            s = s.substring(1, s.length() - 1);
        variableKey = new NamedKeySpecification(s);
        List<AnalysisItem> analysisItems = keyItems.get(s);
        if (analysisItems != null) {
            analysisItem = analysisItems.get(0);
        } else {
            analysisItems = displayItems.get(s);
            if (analysisItems != null) {
                analysisItem = analysisItems.get(0);
            }
        }
        if (analysisItem == null) {
            throw new FunctionException("We could not find a field named " + s);
        }
    }

    public void resolveVariableKey(Map<String, List<AnalysisItem>> keyItems, Map<String, List<AnalysisItem>> displayItems, int aggregationType) {
        String s = getText().trim();
        if(s.startsWith("[") && s.endsWith("]"))
            s = s.substring(1, s.length() - 1);
        variableKey = new AggregateKeySpecification(s, aggregationType);
        List<AnalysisItem> analysisItems = keyItems.get(s);
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
        if (analysisItem == null) {
            throw new FunctionException("We could not find a field named " + s);
        }
    }

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}

    private KeySpecification variableKey;

    private AnalysisItem analysisItem;
}
