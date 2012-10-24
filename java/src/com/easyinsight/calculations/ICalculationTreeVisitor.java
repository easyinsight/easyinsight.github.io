package com.easyinsight.calculations;

import com.easyinsight.core.Value;

public interface ICalculationTreeVisitor {
	void visit(CalculationTreeNode node);
    void visit(NumberNode node);
    void visit(AddNode node);
    void visit(SubtractNode node);
    void visit(MultiplyNode node);
    void visit(DivideNode node);
    void visit(ExponentNode node);
    void visit(VariableNode node);
    void visit(FunctionNode node);
    void visit(StringNode node);

    void visit(EqualsNode node);
    void visit(NotEqualsNode node);
    void visit(GreaterThanNode node);
    void visit(GreaterThanEqualToNode node);
    void visit(LessThanNode node);
    void visit(LessThanEqualToNode node);

    void visit(AndNode node);
    void visit(OrNode node);
    void visit(NotNode node);

    Value getResult();
}
