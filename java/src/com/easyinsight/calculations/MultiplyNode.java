package com.easyinsight.calculations;

import org.antlr.runtime.Token;

public class MultiplyNode extends OperationNode {
	
	public MultiplyNode(Token t) {
		super(t);
	}

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}
}
