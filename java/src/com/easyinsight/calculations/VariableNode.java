package com.easyinsight.calculations;

import org.antlr.runtime.Token;
import com.easyinsight.core.Key;
import com.easyinsight.analysis.AggregateKey;

public class VariableNode extends CalculationTreeNode {

	public VariableNode(Token t){
		super(t);                
    }

    public Key getVariableKey() {
        return variableKey;
    }
    
    public void resolveVariableKey(Resolver r) {
        String s = getText().trim();
        if(s.startsWith("[") && s.endsWith("]"))
            s = s.substring(1, s.length() - 1);
        variableKey = r.getKey(s);
    }

    public void resolveVariableKey(Resolver r, int aggregationType) {
        String s = getText().trim();
        if(s.startsWith("[") && s.endsWith("]"))
            s = s.substring(1, s.length() - 1);
        variableKey = new AggregateKey(r.getKey(s), aggregationType);
    }

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}

    private Key variableKey;
	
}
