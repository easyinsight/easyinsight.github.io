package com.easyinsight.calculations;

import org.antlr.runtime.Token;

public class ExponentNode extends OperationNode {
	public ExponentNode(Token t) {
		super(t);
	}

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}
}
