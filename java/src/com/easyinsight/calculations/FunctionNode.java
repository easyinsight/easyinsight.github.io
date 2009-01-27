package com.easyinsight.calculations;

import org.antlr.runtime.Token;

public class FunctionNode extends OperationNode {
	public FunctionNode(Token t) {
		super(t);
    }

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}

    public IFunction getFunction() {
        return func;
    }

    public void resolveFunction(FunctionFactory f) {
        func = f.createFunction(getChild(0).getText().trim());
    }
    
    private IFunction func;
}