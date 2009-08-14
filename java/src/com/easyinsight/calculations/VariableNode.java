package com.easyinsight.calculations;

import org.antlr.runtime.Token;
import com.easyinsight.core.Key;

public class VariableNode extends CalculationTreeNode {

	public VariableNode(Token t){
		super(t);                
    }

    public Key getVariableKey() {
        return variableKey;
    }
    
    public void resolveVariableKey(Resolver r) {
        String s = getText().trim();
        s = s.substring(1, s.length() - 1);
        variableKey = r.getKey(s);
    }

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}

    private Key variableKey;
	
}
