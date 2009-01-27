package com.easyinsight.calculations;

import org.antlr.runtime.Token;

public class DivideNode extends OperationNode {
	
	public DivideNode(Token t) {
		super(t);
	}

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}
}