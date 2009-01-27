package com.easyinsight.calculations;

import org.antlr.runtime.Token;

public class AddNode extends OperationNode {
	public AddNode(Token t) {
		super(t);
	}

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}
}
