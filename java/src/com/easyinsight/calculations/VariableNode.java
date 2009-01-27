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
        variableKey = r.getKey(getText().trim());
    }

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}

    private Key variableKey;
	
}
