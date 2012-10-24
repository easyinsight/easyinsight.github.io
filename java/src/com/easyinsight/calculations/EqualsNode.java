package com.easyinsight.calculations;

import org.antlr.runtime.Token;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 10/22/12
 * Time: 9:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class EqualsNode extends OperationNode {
    public EqualsNode(Token payload) {
        super(payload);
    }

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
		visitor.visit(this);
	}
}
