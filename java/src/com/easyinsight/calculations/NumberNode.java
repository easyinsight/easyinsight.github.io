package com.easyinsight.calculations;

import org.antlr.runtime.Token;
import com.easyinsight.core.NumericValue;

public class NumberNode extends CalculationTreeNode {

	public NumberNode(Token t) {
		super(t);
        num = java.lang.Double.parseDouble(t.getText());        
    }

    public NumericValue getNumber() {
        return new NumericValue(num);        
    }

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}

    private double num; 
	
}
