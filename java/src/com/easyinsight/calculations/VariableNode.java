package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import org.antlr.runtime.Token;
import com.easyinsight.core.Key;

import java.util.Collection;
import java.util.List;

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
    
    public void resolveVariableKey(Collection<AnalysisItem> allItems) {
        String s = getText().trim();
        if(s.startsWith("[") && s.endsWith("]"))
            s = s.substring(1, s.length() - 1);
        variableKey = new NamedKeySpecification(s);
        for (AnalysisItem item : allItems) {
            if ((item.getKey().toKeyString().equals(s)) ||
                    item.getDisplayName() != null && item.getDisplayName().equals(s)) {
                analysisItem = item;
            }
        }
        if (analysisItem == null) {
            throw new FunctionException("We could not find a field named " + s);
        }
    }

    public void resolveVariableKey(Collection<AnalysisItem> allItems, int aggregationType) {
        String s = getText().trim();
        if(s.startsWith("[") && s.endsWith("]"))
            s = s.substring(1, s.length() - 1);
        variableKey = new AggregateKeySpecification(s, aggregationType);
        for (AnalysisItem item : allItems) {
            if (item.getKey().toKeyString().equals(s)) {
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

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}

    private KeySpecification variableKey;

    private AnalysisItem analysisItem;
}
