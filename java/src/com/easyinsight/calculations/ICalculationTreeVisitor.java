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

    Value getResult();
}
