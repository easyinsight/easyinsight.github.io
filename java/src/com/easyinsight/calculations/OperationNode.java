package com.easyinsight.calculations;

import org.antlr.runtime.Token;

public class OperationNode extends CalculationTreeNode {
	public OperationNode(Token t) {
		super(t);
	}

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}

}
