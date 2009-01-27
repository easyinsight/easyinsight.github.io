package com.easyinsight.calculations;

import org.antlr.runtime.Token;

public class SubtractNode extends OperationNode {
	public SubtractNode(Token t) {
		super(t);
	}

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}
}
