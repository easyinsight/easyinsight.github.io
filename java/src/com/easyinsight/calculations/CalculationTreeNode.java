package com.easyinsight.calculations;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

public abstract class CalculationTreeNode extends CommonTree {
	public CalculationTreeNode(Token t) {
		super(t);
	}
	
	public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}
}
